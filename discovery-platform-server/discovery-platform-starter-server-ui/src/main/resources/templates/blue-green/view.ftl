<@compress single_line=true>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <style type="text/css">
    div#mountNode{
       margin-left: 50px;
    }
    div#mountNode img{
       width:auto;height:auto;max-width: 100%;max-height: 100%;
    }
  </style>
  <#include "../common/layui.ftl">
</head>
<body>
<div id="mountNode"></div>
<script>/*Fixing iframe window.innerHeight 0 issue in Safari*/document.body.clientHeight;</script>
<script src="${ctx}/js/g6/g6.min.js"></script>
<script src="${ctx}/js/g6/build.g6.js"></script>
<script src="${ctx}/js/g6/dagre.min.js"></script>
<script>
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

  /**
   * 自定义节点
   */
  G6.registerNode("node", {
    drawShape: function drawShape(cfg, group) {
      if (cfg.type === 'begin') {
        return group.addShape('dom', {
          attrs: {
            x: -24,
            y: 60,
            width: 48,
            height: 48,
            html: "<img src='${ctx}/images/graph/gateway_black_64.png'/>"
          }
        });
      }

      var color = "green";
      if (cfg.id.indexOf(basicRouteId + '_') === 0) {
          color = "yellow";
      } else if (blueRoutes.includes(cfg.id.substr(0, cfg.id.indexOf('_')))) {
          color = "blue"
      }

      var rect = group.addShape('dom', {
          attrs: {
            x: -16,
            y: 0,
            width: 36,
            height: 36,
            html: "<img src='${ctx}/images/graph/service_" + color +"_64.png'/>"
          }
        });

      if (cfg.routeId) {
        group.addShape('text', {
          attrs: {
          x: -30,
          y: -35,
          text: cfg.routeId || '',
          fill: cfg.textColor ? cfg.textColor : '#666666',
          autoRotate: true,
          refY: 10,
          }
        });
      }

      if (cfg.condition) {
        group.addShape('text', {
          attrs: {
          x: -30,
          y: -15,
          text: cfg.condition || '',
          fill: cfg.textColor ? cfg.textColor : '#666666',
          autoRotate: true,
          refY: 10,
          }
        });
      }

      group.addShape('text', {
        attrs: {
        x: -30,
        y: 75,
        text: cfg.value ? 'version=' + cfg.value : '',
        fill: cfg.textColor ? cfg.textColor : '#666666',
        autoRotate: true,
        }
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
        y: (startPoint.y + 20)
      };

      var color = "#00A3AF";
      if (cfg.target.indexOf(basicRouteId + '_') === 0) {
          color = "#B9AE12";
      } else if (blueRoutes.includes(cfg.target.substr(0, cfg.target.indexOf('_')))) {
          color = "#1296DB";
      }

      var path = group.addShape("path", {
        attrs: {
          path: [
          ["M", startPoint.x, startPoint.y],
          ["Q", controlPoint.x, controlPoint.y + 8, centerPoint.x, centerPoint.y],
          ["T", endPoint.x, endPoint.y - 8],
          ["L", endPoint.x, endPoint.y]],
          stroke: color,
          lineWidth: 1.6,
          endArrow: {
            path: "M -3,0 L 3,3 L 3,-3 Z",
            d: -14,
            lineWidth: 3,
          }
        }
      });

      return path;
    }
  });

  var graph = new G6.Graph({
    renderer: "svg",
    container: "mountNode",
    width: 950,
    height: 550,
    layout: {
      type: "dagre",
      nodesep: 100,
      ranksepFunc : (d) => {
        if (d.id === "begin") {
          return 150;
        }
        return 20;
      },
      controlPoints: true
    },
    modes: {
      default: ['drag-canvas', 'zoom-canvas']
    },
    defaultNode: {
      shape: "node",
      labelCfg: {
        style: {
          fill: "#666666",
          fontSize: 14
        },
        offset: 30,
        position: 'bottom'
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

  var data = ${config} || {nodes: [{id: "noConfig", label: "未配置"}]};
  var index = 0;
  var blueRoutes = [];
  var basicRouteId;
  function getBasicRoute(data) {
     for (var i = 0; i < data.nodes.length; i ++) {
        var node = data.nodes[i];
        if (node.routeId && !node.condition) {
            basicRouteId = node.routeId;
        } else if (node.routeId) {
            if (index % 2 === 1) {
                blueRoutes.push(node.routeId);
            }
            index ++;
        }
     }
  }

  getBasicRoute(data);
  graph.data(data);
  graph.render();
  graph.fitView();
  </script>
</body>
</html>
</@compress>