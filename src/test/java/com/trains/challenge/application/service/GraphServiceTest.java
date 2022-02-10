package com.trains.challenge.application.service;

import com.trains.challenge.domain.Graph;
import com.trains.challenge.domain.Node;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class GraphServiceTest {

    Node nodeA = new Node("A");
    Node nodeB = new Node("B");
    Node nodeC = new Node("C");
    Node nodeD = new Node("D");
    Node nodeE = new Node("E");
    private IGraphService graphService;
    private Graph graph;

    @BeforeEach
    public void setUp() {
        graphService = new GraphService();
        graph = new Graph();
        loadData();

    }

    private void loadData() {
        nodeA.addDestination(nodeB, 5);
        nodeB.addDestination(nodeC, 4);
        nodeC.addDestination(nodeD, 8);
        nodeD.addDestination(nodeC, 8);
        nodeD.addDestination(nodeE, 6);
        nodeA.addDestination(nodeD, 5);
        nodeC.addDestination(nodeE, 2);
        nodeE.addDestination(nodeB, 3);
        nodeA.addDestination(nodeE, 7);
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);

    }

    //Punto 1 A - B - C
    @SneakyThrows
    @Test
    public void testShortestPathABCSuccessfully() {
        Integer distanceABC = 0;
        distanceABC += graphService.getDistance(nodeA, "B", graph);
        //Distance A+B
        Assertions.assertEquals(5, distanceABC);
        distanceABC += graphService.getDistance(nodeB, "C", graph);
        //Distande A+B+C
        System.out.println("Punto 1:  Distancia A - B - C: " + distanceABC);
        Assertions.assertEquals(9, distanceABC);
    }

    //Punto 2 A - D
    @SneakyThrows
    @Test
    public void testShortestPathADSuccessfully() {
        Integer distanceAD = 0;
        distanceAD += graphService.getDistance(nodeA, "D", graph);
        System.out.println("Punto 2:  Distancia A - D: " + distanceAD);
        Assertions.assertEquals(5, distanceAD);
    }

    //Punto 3 A - D - C
    @SneakyThrows
    @Test
    public void testShortestPathADCSuccessfully() {
        Integer distanceADC = 0;
        distanceADC += graphService.getDistance(nodeA, "D", graph);
        //Distance A+D
        Assertions.assertEquals(5, distanceADC);
        distanceADC += graphService.getDistance(nodeD, "C", graph);
        //Distance A+D+C
        System.out.println("Punto 3:  Distancia A - D - C: " + distanceADC);
        Assertions.assertEquals(13, distanceADC);
    }

    //Punto 4 A - E - B - C - D
    @SneakyThrows
    @Test
    public void getShortestPathAEBCDSuccessfully() {
        Integer resultAEBCD = 0;
        resultAEBCD += graphService.getDistance(nodeA, "E", graph);
        resultAEBCD += graphService.getDistance(nodeE, "B", graph);
        resultAEBCD += graphService.getDistance(nodeB, "C", graph);
        resultAEBCD += graphService.getDistance(nodeC, "D", graph);
        System.out.println("Punto 4:  Distancia A - E - B - C - D: " + resultAEBCD);
        Assertions.assertEquals(22, resultAEBCD);

    }

    //Punto 5 A - E - D
    @Test
    public void testShortestPathAEDSuccessfully() throws Exception {
        graphService.getDistance(nodeA, "E", graph);
        Exception exception = Assertions.assertThrows(Exception.class, () -> graphService.getDistance(nodeE, "D", graph));
        System.out.println("Punto 5:  Distancia A - E - D: " + exception.getMessage());
        Assertions.assertEquals("NO SUCH ROUTE", exception.getMessage());

    }

    //Punto 8 A to C
    @SneakyThrows
    @Test
    public void testShortestPathACSuccessfully() {
        var resultAC = 0;
        resultAC += graphService.getShortestPath(nodeA, "C", graph);
        System.out.println("Punto 8:  Distancia A - C: " + resultAC);
        Assertions.assertEquals(9, resultAC);
    }

    //Punto 9 B to B
    @Test
    public void testShortestPathBBSuccessfully() {
        var resultBB = 0;
        resultBB += graphService.sameDestinationRoute(nodeB, graph);
        System.out.println("Punto 9:  Distancia B - B: " + resultBB);
        Assertions.assertEquals(9, resultBB);
    }

}
