package com.trains.challenge.application.service;

import com.trains.challenge.domain.Graph;
import com.trains.challenge.domain.Node;

public interface IGraphService {

    Integer getDistance(Node nodeSource, String nameNodeTarget, Graph graph) throws Exception;

    /**
     * Calcula la ruta mas corta
     *
     * @param nodeSource
     * @param nameNodeTarget
     * @param graph
     * @return
     */
    Integer getShortestPath(Node nodeSource, String nameNodeTarget, Graph graph) throws Exception;

    /**
     * Calcula la ruta mas corta iniciando desde un punto X llegando al mismo punto
     *
     * @param nodeSource
     * @param graph
     * @return
     */
    Integer sameDestinationRoute(Node nodeSource, Graph graph);

}
