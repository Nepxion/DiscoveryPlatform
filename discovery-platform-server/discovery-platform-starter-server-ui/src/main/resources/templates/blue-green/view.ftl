<@compress single_line=true>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <style type="text/css">
    div#mountNode{
       margin-left: 100px;
    }
  </style>
  <#include "../common/layui.ftl">
</head>
<body>
<div id="mountNode"></div>
<script src="https://gw.alipayobjects.com/os/antv/pkg/_antv.g6-3.7.1/dist/g6.min.js"></script>
<!--  <script>/*Fixing iframe window.innerHeight 0 issue in Safari*/document.body.clientHeight;</script>-->
<script src="https://gw.alipayobjects.com/os/antv/pkg/_antv.g6-3.1.1/build/g6.js"></script>
<script src="https://gw.alipayobjects.com/os/lib/dagre/0.8.4/dist/dagre.min.js"></script>
<script>
  var _extends = Object.assign || function(target) {
    for (var i = 1; i < arguments.length; i++) {
      var source = arguments[i];
      for (var key in source) {
        if (Object.prototype.hasOwnProperty.call(source, key)) {
          target[key] = source[key];
        }
      }
    }
    return target;
  };

  /**
   * 本案例演示如何使用G6自定义流程图：
   * 1、如何使用G6绘制流程图；
   * 2、如何在贝塞尔曲线上面自定义icon；
   * 3、如何响应贝塞尔曲线上icon的点击事件。
   *
   * by 一之
   *
   */

  /**
   * node 特殊属性
   */
  var nodeExtraAttrs = {
    begin: {
      fill: "#9FD4FB",
    },
    end: {
      fill: "#C2E999"
    }
  };

  var data = {
    nodes: [{
      id: "1",
      label: "",
      type: "begin"
  }, {
      id: "2",
      label: "服务A"
  }, {
      id: "3",
      label: "服务A"
  }, {
      id: "4",
      label: "服务B"
  }, {
      id: "5",
      label: "服务B"
  }, {
      id: "6",
      label: "服务C"
  }],
    edges: [{
      source: "1",
      target: "2",
      version: "v1.1"
  }, {
      source: "1",
      target: "3",
      version: "v1.0"
  }, {
      source: "2",
      target: "5",
      version: "v1.1"
  }, {
      source: "5",
      target: "6",
      version: "v1.0"
  }, {
      source: "3",
      target: "4",
      version: "v1.2"
  }]
  };

  /**
   * 自定义节点
   */
  G6.registerNode("node", {
    drawShape: function drawShape(cfg, group) {
      if (cfg.type === 'begin') {
        var circle = group.addShape("circle", {
          attrs: {
            r: 12,
            x: 0,
            y: 0,
            fill: "#9FD4FB",
            fillOpacity: 1
          }
        });
        return circle;
      }

      var rect = group.addShape("rect", {
        attrs: _extends({
          x: -75,
          y: -25,
          width: 150,
          height: 50,
          radius: 4,
          fill: "#FFD591",
          fillOpacity: 1
        }, nodeExtraAttrs[cfg.type])
      });
      return rect;
    },
    setState: function setState(name, value, item) {
      var group = item.getContainer();
      var shape = group.get("children")[0];

      if (name === "selected") {
        if (value) {
          shape.attr("fill", "#F6C277");
        } else {
          shape.attr("fill", "#FFD591");
        }
      }
    },

    getAnchorPoints: function getAnchorPoints() {
      return [[0.5, 0], [0.5, 1]];
    }
  }, "single-shape");

  /**
   * 自定义 edge 中心关系节点
   */
  G6.registerNode("statusNode", {
    drawShape: function drawShape(cfg, group) {
      var circle = group.addShape("circle", {
        attrs: {
          x: 0,
          y: 0,
          r: 6,
          fill: cfg.active ? "#AB83E4" : "#ccc"
        }
      });
      return circle;
    }
  }, "single-shape");

  /**
   * 自定义带箭头的贝塞尔曲线 edge
   */
  G6.registerEdge("line-with-arrow", {
    itemType: "edge",
    draw: function draw(cfg, group) {
      var startPoint = cfg.startPoint;
      var endPoint = cfg.endPoint;
      var centerPoint = {
        x: (startPoint.x + endPoint.x) / 2,
        y: (startPoint.y + endPoint.y) / 2
      };
      var controlPoint = {
        x: (startPoint.x),
        y: (startPoint.y + 50)
      };
      var path = group.addShape("path", {
        attrs: {
          path: [
          ["M", startPoint.x, startPoint.y],
          ["Q", controlPoint.x, controlPoint.y + 8, centerPoint.x, centerPoint.y],
          ["T", endPoint.x, endPoint.y - 8],
          ["L", endPoint.x, endPoint.y]],
          stroke: "#ccc",
          lineWidth: 1.6,
          endArrow: {
            path: "M 4,0 L -4,-4 L -4,4 Z",
            d: 4
          }
        }
      });
      var source = cfg.source,
        target = cfg.target;
      group.addShape("circle", {
        attrs: {
          id: "statusNode" + source + "-" + target,
          r: 6,
          x: centerPoint.x,
          y: centerPoint.y,
          fill: cfg.active ? "#AB83E4" : "#ccc"
        }
      });

      group.addShape('text', {
        attrs: {
        x: centerPoint.x,
        y: centerPoint.y - 12,
        text: cfg.version || '',
        fill: cfg.textColor ? cfg.textColor : '#666666',
        autoRotate: true,
        refY: 10,
        }
      });

      return path;
    }
  });

  var graph = new G6.Graph({
    container: "mountNode",
    width: 800,
    height: 700,
    layout: {
      type: 'dagre',
      linkDistance: 100
    },
    modes: {
      default: ['drag-canvas']
    },
    defaultNode: {
      shape: "node",
      labelCfg: {
        style: {
          fill: "#fff",
          fontSize: 14
        }
      }
    },
    defaultEdge: {
      shape: "line-with-arrow",
      style: {
        endArrow: true,
        lineWidth: 2,
        stroke: "#ccc"
      }
    }
  });

  graph.data(data);
  graph.render();

  graph.on('edge:click', function(evt) {
    var target = evt.target;
    var type = target.get('type');
    if (type === 'circle') {
      alert('你点击的是边上的圆点');
    }
  });
  </script>
</body>
</html>
</@compress>