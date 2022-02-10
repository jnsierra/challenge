package com.trains.challenge.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Graph {
    private Set<Node> nodes = new HashSet<>();

    public void addNode(Node nodeFirst) {
        nodes.add(nodeFirst);
    }
}
