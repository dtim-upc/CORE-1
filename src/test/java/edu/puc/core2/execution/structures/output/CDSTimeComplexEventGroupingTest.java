package edu.puc.core2.execution.structures.output;

import edu.puc.core2.execution.structures.CDS.time.CDSNodeManager;
import edu.puc.core2.execution.structures.CDS.time.CDSTimeNode;
import edu.puc.core2.parser.plan.cea.Transition;
import edu.puc.core2.runtime.events.Event;
import edu.puc.core2.util.DistributionConfiguration;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.misc.Triple;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CDSTimeComplexEventGroupingTest {
    private static final Event event0 = new Event(0);
    private static final Event event1 = new Event(1);
    private static final Event event2 = new Event(2);
    private static final Event event3 = new Event(3);
    private static final Event event4 = new Event(4);
    private static final Event event5 = new Event(5);

    private static final long ignoreWindowDelta = Long.MAX_VALUE;
    private static final long ignoreCurrentTime = -1;

    @BeforeClass
    public static void setUp() {}

    // ************************* Regular enumeration ******************************

    /*
             3
             |
         ___ V2
     (l)/    |
       2  __ V1
       | /  /(l)
       1  2'
       | /
       0
       |
     bottom
    */
    public static Pair<CDSTimeNode, List<ComplexEvent>> getCDS1() {
        CDSNodeManager m = new CDSNodeManager();
        CDSTimeNode bottom = m.createBottomNode(0);
        CDSTimeNode zero = m.createOutputNode(bottom, Transition.TransitionType.BLACK, event0, 0);
        CDSTimeNode one = m.createOutputNode(zero, Transition.TransitionType.BLACK, event1, 1);
        CDSTimeNode two = m.createOutputNode(one, Transition.TransitionType.BLACK, event2, 2);
        CDSTimeNode two2 = m.createOutputNode(zero, Transition.TransitionType.BLACK, event2, 2);
        CDSTimeNode v1 = m.createUnionNode(two2, one);
        CDSTimeNode v2 = m.createUnionNode(two, v1);
        CDSTimeNode three = m.createOutputNode(v2, Transition.TransitionType.BLACK, event3, 3);

        ComplexEvent complexEvent1 = new ComplexEvent();
        complexEvent1.push(event3, null);
        complexEvent1.push(event2, null);
        complexEvent1.push(event1, null);
        complexEvent1.push(event0, null);

        ComplexEvent complexEvent2 = new ComplexEvent();
        complexEvent2.push(event3, null);
        complexEvent2.push(event2, null);
        complexEvent2.push(event0, null);

        ComplexEvent complexEvent3 = new ComplexEvent();
        complexEvent3.push(event3, null);
        complexEvent3.push(event1, null);
        complexEvent3.push(event0, null);

        return new Pair<>(three, List.of(complexEvent1, complexEvent2, complexEvent3));
    }

    @Test
    public void testIterator() {
        Pair<CDSTimeNode, List<ComplexEvent>> pair = getCDS1();
        List<ComplexEvent> expectedComplexEvents = pair.b;
        CDSTimeComplexEventGrouping complexEventGrouping = new CDSTimeComplexEventGrouping(null, 0, ignoreWindowDelta, ignoreCurrentTime, Optional.empty());
        complexEventGrouping.addCDSNode(pair.a);
        int i = 0;
        for(ComplexEvent ce: complexEventGrouping) {
            assertEquals("ComplexEvent" + i, expectedComplexEvents.get(i), ce);
            ++i;
        }
        assertEquals("Number of complex events", 3, i);
    }

    /*
                       4
                       |
                       V4
                 (l) / | (l)
                3 - V3 |
                | /    |
              _ V2    /
        (l) /   /     |
       2 - V1  /(l)   |
       | /    /      /
       1 __ 2'   __ 3'    4'
       |/ ______/        /
       0/______________ /
       |
     bottom
    */
    public static Pair<List<CDSTimeNode>, List<ComplexEvent>> getCDS2() {
        CDSNodeManager m = new CDSNodeManager();
        CDSTimeNode bottom0 = m.createBottomNode(0);
        CDSTimeNode zero = m.createOutputNode(bottom0, Transition.TransitionType.BLACK, event0, 0);
        CDSTimeNode one = m.createOutputNode(zero, Transition.TransitionType.BLACK, event1, 1);
        CDSTimeNode two = m.createOutputNode(one, Transition.TransitionType.BLACK, event2, 2);
        CDSTimeNode two2 = m.createOutputNode(zero, Transition.TransitionType.BLACK, event2, 2);
        CDSTimeNode v1 = m.createUnionNode(two, one);
        CDSTimeNode v2 = m.createUnionNode(two2, v1);
        CDSTimeNode three = m.createOutputNode(v2, Transition.TransitionType.BLACK, event3, 3);
        CDSTimeNode three2 = m.createOutputNode(zero, Transition.TransitionType.BLACK, event3, 3);
        CDSTimeNode v3 = m.createUnionNode(three, v2);
        CDSTimeNode v4 = m.createUnionNode(three2, v3);
        CDSTimeNode four = m.createOutputNode(v4, Transition.TransitionType.BLACK, event4, 4);
        CDSTimeNode four2 = m.createOutputNode(zero, Transition.TransitionType.BLACK, event4, 4);

        ComplexEvent complexEvent1 = new ComplexEvent();
        complexEvent1.push(event4, null);
        complexEvent1.push(event3, null);
        complexEvent1.push(event0, null);

        ComplexEvent complexEvent2 = new ComplexEvent();
        complexEvent2.push(event4, null);
        complexEvent2.push(event3, null);
        complexEvent2.push(event2, null);
        complexEvent2.push(event0, null);

        ComplexEvent complexEvent3 = new ComplexEvent();
        complexEvent3.push(event4, null);
        complexEvent3.push(event3, null);
        complexEvent3.push(event2, null);
        complexEvent3.push(event1, null);
        complexEvent3.push(event0, null);

        ComplexEvent complexEvent4 = new ComplexEvent();
        complexEvent4.push(event4, null);
        complexEvent4.push(event3, null);
        complexEvent4.push(event1, null);
        complexEvent4.push(event0, null);

        ComplexEvent complexEvent5 = new ComplexEvent();
        complexEvent5.push(event4, null);
        complexEvent5.push(event2, null);
        complexEvent5.push(event0, null);

        ComplexEvent complexEvent6 = new ComplexEvent();
        complexEvent6.push(event4, null);
        complexEvent6.push(event2, null);
        complexEvent6.push(event1, null);
        complexEvent6.push(event0, null);

        ComplexEvent complexEvent7 = new ComplexEvent();
        complexEvent7.push(event4, null);
        complexEvent7.push(event1, null);
        complexEvent7.push(event0, null);

        ComplexEvent complexEvent8 = new ComplexEvent();
        complexEvent8.push(event4, null);
        complexEvent8.push(event0, null);

        return new Pair<>(
                List.of(four, four2),
                List.of(complexEvent1, complexEvent2, complexEvent3, complexEvent4,
                        complexEvent5, complexEvent6, complexEvent7, complexEvent8)
        );
    }


    @Test
    public void testIteratorMultipleFinalStates() {
        Pair<List<CDSTimeNode>, List<ComplexEvent>> pair = getCDS2();
        List<ComplexEvent> expectedComplexEvents = pair.b;
        CDSTimeComplexEventGrouping complexEventGrouping = new CDSTimeComplexEventGrouping(null, 0, ignoreWindowDelta, ignoreCurrentTime, Optional.empty());
        for(CDSTimeNode cdsRoot : pair.a) {
            complexEventGrouping.addCDSNode(cdsRoot);
        }
        int i = 0;
        for(ComplexEvent ce: complexEventGrouping) {
            assertEquals("ComplexEvent" + i, expectedComplexEvents.get(i), ce);
            ++i;
        }
        assertEquals("Number of complex events", expectedComplexEvents.size(), i);
    }

    // ************************* Ranged Enumeration ******************************

    @Test
    public void testRangedIterator() {
        int processes = 2;
        int i = 0;
        Pair<CDSTimeNode, List<ComplexEvent>> pair = getCDS1();
        List<ComplexEvent> expectedComplexEvents = pair.b;

        // Process 0
        CDSTimeComplexEventGrouping complexEventGrouping = new CDSTimeComplexEventGrouping(null, 0, ignoreWindowDelta, ignoreCurrentTime, Optional.of(new DistributionConfiguration(0, processes)));
        complexEventGrouping.addCDSNode(pair.a);
        for(ComplexEvent ce: complexEventGrouping) {
            assertEquals("ComplexEvent" + i, expectedComplexEvents.get(i), ce);
            ++i;
        }
        assertEquals("Number of complex events", 2, i);

        // Process 1
        CDSTimeComplexEventGrouping complexEventGrouping2 = new CDSTimeComplexEventGrouping(null, 0, ignoreWindowDelta, ignoreCurrentTime, Optional.of(new DistributionConfiguration(1, processes)));
        complexEventGrouping2.addCDSNode(pair.a);
        for(ComplexEvent ce: complexEventGrouping2) {
            assertEquals("ComplexEvent" + i, expectedComplexEvents.get(i), ce);
            ++i;
        }
        assertEquals("Number of complex events", 3, i);
    }

    private List<Integer> zeroToN(int n) {
        List<Integer> r = new ArrayList<>(n);
        int i = 0;
        while (i < n) {
            r.add(i);
            i++;
        }
        return r;
    }

    @Test
    public void testRangedIteratorMultipleFinalStates() {
        int processes = 3;
        Pair<List<CDSTimeNode>, List<ComplexEvent>> pair = getCDS2();
        List<ComplexEvent> expectedComplexEvents = List.of(
                // Process 0 (notice that process 0 also process 2nd CDS complex events)
                pair.b.get(0),
                pair.b.get(1),
                pair.b.get(2),
                pair.b.get(7),
                // Process 1
                pair.b.get(3),
                pair.b.get(4),
                pair.b.get(5),
                // Process 2
                pair.b.get(6)
        );
        int[] expectedAmountByProcess = {4, 7, 8};

        int i = 0;
        for(int process : zeroToN(processes)) {
            CDSTimeComplexEventGrouping complexEventGrouping = new CDSTimeComplexEventGrouping(null, 0, ignoreWindowDelta, ignoreCurrentTime, Optional.of(new DistributionConfiguration(process, processes)));
            for(CDSTimeNode cdsRoot : pair.a) {
                complexEventGrouping.addCDSNode(cdsRoot);
            }
            for(ComplexEvent ce: complexEventGrouping) {
                if (ce != null) {
                    assertEquals("ComplexEvent" + i, expectedComplexEvents.get(i), ce);
                    ++i;
                }
            }
            assertEquals("Number of complex events", expectedAmountByProcess[process], i);
        }
    }

    /*
       2
       |
       1
       |
       0
       |
     bottom
    */
    public static Pair<CDSTimeNode, ComplexEvent> getCDS3() {
        CDSNodeManager m = new CDSNodeManager();
        CDSTimeNode bottom0 = m.createBottomNode(0);
        CDSTimeNode zero = m.createOutputNode(bottom0, Transition.TransitionType.BLACK, event0, 0);
        CDSTimeNode one = m.createOutputNode(zero, Transition.TransitionType.BLACK, event1, 1);
        CDSTimeNode two = m.createOutputNode(one, Transition.TransitionType.BLACK, event2, 2);

        ComplexEvent complexEvent = new ComplexEvent();
        complexEvent.push(event2, null);
        complexEvent.push(event1, null);
        complexEvent.push(event0, null);

        return new Pair<>(two, complexEvent);
    }

    // Corner case: when the complex event doesn't have union nodes
    @Test
    public void testRangedIteratorCornerCase() {
        int processes = 2;
        int i = 0;
        Pair<CDSTimeNode, ComplexEvent> pair = getCDS3();
        ComplexEvent expectedComplexEvent = pair.b;

        // Process 0
        CDSTimeComplexEventGrouping complexEventGrouping = new CDSTimeComplexEventGrouping(null, 0, ignoreWindowDelta, ignoreCurrentTime, Optional.of(new DistributionConfiguration(0, processes)));
        complexEventGrouping.addCDSNode(pair.a);
        for(ComplexEvent ce: complexEventGrouping) {
            assertEquals("ComplexEvent", expectedComplexEvent, ce);
            i++;
        }
        assertEquals("Number of complex events", 1, i);

        // Process 1
        CDSTimeComplexEventGrouping complexEventGrouping2 = new CDSTimeComplexEventGrouping(null, 0, ignoreWindowDelta, ignoreCurrentTime, Optional.of(new DistributionConfiguration(1, processes)));
        complexEventGrouping2.addCDSNode(pair.a);
        for(ComplexEvent ce: complexEventGrouping2) {
            assertNull(ce);
            i++;
        }
        assertEquals("Number of complex events", 2, i);
    }

    // ************************* Time-Window Enumeration ******************************

    /*
             3
             |
         ___ V
     (l)/    |(r)
       2     2
       |     |
       1     1
       |     |
       0     b (1)
       |
       b (0)
    */
    public static Triple<CDSTimeNode, List<ComplexEvent>, Long> getCDSTime1() {
        CDSNodeManager m = new CDSNodeManager();
        CDSTimeNode bottom0 = m.createBottomNode(0);
        CDSTimeNode zero = m.createOutputNode(bottom0, Transition.TransitionType.BLACK, event0, 0);
        CDSTimeNode oneL = m.createOutputNode(zero, Transition.TransitionType.BLACK, event1, 1);
        CDSTimeNode twoL = m.createOutputNode(oneL, Transition.TransitionType.BLACK, event2, 2);
        CDSTimeNode bottom1 = m.createBottomNode(1);
        CDSTimeNode oneR = m.createOutputNode(bottom1, Transition.TransitionType.BLACK, event1, 1);
        CDSTimeNode twoR = m.createOutputNode(oneR, Transition.TransitionType.BLACK, event2, 2);
        CDSTimeNode v = m.createUnionNode(twoL, twoR);
        CDSTimeNode three = m.createOutputNode(v, Transition.TransitionType.BLACK, event3, 3);

        long timeWindow = 2;
        // Notice CE {0,1,2,3} is discarded by the time-windows.
        ComplexEvent complexEvent = new ComplexEvent();
        complexEvent.push(event3, null);
        complexEvent.push(event2, null);
        complexEvent.push(event1, null);

        return new Triple<>(three, List.of(complexEvent), timeWindow);
    }
    @Test
    public void testTimeWindowsIterator() {
        Triple<CDSTimeNode, List<ComplexEvent>, Long> triplet = getCDSTime1();
        List<ComplexEvent> expectedComplexEvents = triplet.b;
        CDSTimeComplexEventGrouping complexEventGrouping = new CDSTimeComplexEventGrouping(null, 0, triplet.c, triplet.a.getMax(), Optional.empty());
        complexEventGrouping.addCDSNode(triplet.a);
        int i = 0;
        for(ComplexEvent ce: complexEventGrouping) {
            if (ce != null) {
                assertEquals("ComplexEvent" + i, expectedComplexEvents.get(i), ce);
                ++i;
            }
        }
        assertEquals("Number of complex events", expectedComplexEvents.size(), i);
    }

    /*
                  5
                  |
                _ V3
              /   | (l)
             3   /
             |   4
         ___ V1  |
     (l)/    |   V2
       2     2  /| (l)
       |     | / 3
       1     1   |
       |     |   b (3)
       0     b (1)
       |
       b (0)
    */
    public static List<Triple<CDSTimeNode, List<ComplexEvent>, Long>> getCDSTime2() {
        CDSNodeManager m = new CDSNodeManager();
        CDSTimeNode bottom0 = m.createBottomNode(0);
        CDSTimeNode bottom1 = m.createBottomNode(1);
        CDSTimeNode bottom3 = m.createBottomNode(3);

        CDSTimeNode zero = m.createOutputNode(bottom0, Transition.TransitionType.BLACK, event0, 0);
        CDSTimeNode oneL = m.createOutputNode(zero, Transition.TransitionType.BLACK, event1, 1);
        CDSTimeNode twoL = m.createOutputNode(oneL, Transition.TransitionType.BLACK, event2, 2);
        CDSTimeNode oneR = m.createOutputNode(bottom1, Transition.TransitionType.BLACK, event1, 1);
        CDSTimeNode twoR = m.createOutputNode(oneR, Transition.TransitionType.BLACK, event2, 2);
        CDSTimeNode v1 = m.createUnionNode(twoL, twoR);
        CDSTimeNode threeL = m.createOutputNode(v1, Transition.TransitionType.BLACK, event3, 3);
        CDSTimeNode threeR = m.createOutputNode(bottom3, Transition.TransitionType.BLACK, event3, 3);
        CDSTimeNode v2 = m.createUnionNode(threeR, oneR);
        CDSTimeNode four = m.createOutputNode(v2, Transition.TransitionType.BLACK, event4, 4);
        CDSTimeNode v3 = m.createUnionNode(four, threeL);
        CDSTimeNode five = m.createOutputNode(v3, Transition.TransitionType.BLACK, event5, 5);

        List<Triple<CDSTimeNode, List<ComplexEvent>, Long>> r = new ArrayList<>();

        {
            long timeWindow = 4;
            ComplexEvent complexEvent1 = new ComplexEvent();
            complexEvent1.push(event5, null);
            complexEvent1.push(event4, null);
            complexEvent1.push(event3, null);
            ComplexEvent complexEvent2 = new ComplexEvent();
            complexEvent2.push(event5, null);
            complexEvent2.push(event4, null);
            complexEvent2.push(event1, null);
            ComplexEvent complexEvent3 = new ComplexEvent();
            complexEvent3.push(event5, null);
            complexEvent3.push(event3, null);
            complexEvent3.push(event2, null);
            complexEvent3.push(event1, null);
            r.add(new Triple<>(five, List.of(complexEvent1, complexEvent2, complexEvent3), timeWindow));
        }

        {
            long timeWindow = 3;
            ComplexEvent complexEvent1 = new ComplexEvent();
            complexEvent1.push(event5, null);
            complexEvent1.push(event4, null);
            complexEvent1.push(event3, null);
            r.add(new Triple<>(five, List.of(complexEvent1), timeWindow));
        }

        return r;
    }
    @Test
    public void testTimeWindowsIterator2() {
        for(Triple<CDSTimeNode, List<ComplexEvent>, Long> triplet : getCDSTime2()) {
            List<ComplexEvent> expectedComplexEvents = triplet.b;
            CDSTimeComplexEventGrouping complexEventGrouping = new CDSTimeComplexEventGrouping(null, 0, triplet.c, triplet.a.getMax(), Optional.empty());
            complexEventGrouping.addCDSNode(triplet.a);
            int i = 0;
            for(ComplexEvent ce: complexEventGrouping) {
                if (ce != null) {
                    assertEquals("ComplexEvent" + i, expectedComplexEvents.get(i), ce);
                    ++i;
                }
            }
            assertEquals("Number of complex events", expectedComplexEvents.size(), i);
        }
    }

    // ************************* Time-Window & Ranged Enumeration ******************************

    // TODO
}
