package com.trains.challenge.application.service;

import com.trains.challenge.domain.Graph;
import com.trains.challenge.domain.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class GraphService implements IGraphService {

    private static Logger logger = LogManager.getLogger(GraphService.class);

    @Override
    public Integer sameDestinationRoute(Node nodeSource, Graph graph) {
        logger.info("sameDestinationRoute | Inicia con el nodo origen: {}", nodeSource.getName());
        List<Integer> distance = new ArrayList<>();
        nodeSource.getAdjacentNodes().forEach((key, value) -> {
            Integer result = 0;
            try {
                logger.info("sameDestinationRoute | Inicia busqueda de la ruta mas corta desde {} hasta {}", key.getName(), nodeSource.getName());
                result += getShortestPath(key, nodeSource.getName(), graph);
                logger.info( "sameDestinationRoute | Resultado de la busqueda {} ", result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            result += value;
            distance.add(result);
        });
        Integer result = distance.stream().mapToInt(item -> item).min().getAsInt();
        logger.info("sameDestinationRoute | Obtine el valor minimo de la lista rutas {}", result);
        return result;
    }

    @Override
    public Integer getDistance(Node nodeSource, String nameNodeTarget, Graph graph) throws Exception {
        AtomicReference<Integer> result = new AtomicReference<>(0);
        calculateShortestPath(nodeSource);
        graph.getNodes().stream()
                .filter(node -> node.getName().equalsIgnoreCase(nodeSource.getName()))
                .map(nodeAdjacent -> nodeAdjacent.getAdjacentNodes().entrySet())
                .flatMap(Collection::stream)
                .forEach(stream -> {
                    if (stream.getKey().getName().equalsIgnoreCase(nameNodeTarget)) {
                        result.set(stream.getValue());
                    }
                });

        if (result.get() == 0) {
            throw new Exception("NO SUCH ROUTE");
        }
        return result.get();

    }

    @Override
    public Integer getShortestPath(Node nodeSource, String nameNodeTarget, Graph graph) throws Exception {
        calculateShortestPath(nodeSource);
        return graph.getNodes().stream()
                .filter(nodeResult -> nodeResult.getName().equalsIgnoreCase(nameNodeTarget))
                .findFirst()
                .map(Node::getDistance).orElseThrow(Exception::new);

    }

    private void calculateShortestPath(Node nodeSource) {
        nodeSource.setDistance(0);
        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();
        unsettledNodes.add(nodeSource);
        while (unsettledNodes.size() != 0) {
            var currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            currentNode.getAdjacentNodes().forEach((adjacentNode, edgeWeight) -> {
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            });
            settledNodes.add(currentNode);
        }
    }


    private Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        var lowestDistance = Integer.MAX_VALUE;
        for (Node node : unsettledNodes) {
            var nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }


    private void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {

        var sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            var shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
}
