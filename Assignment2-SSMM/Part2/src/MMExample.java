//Simple example to demonstrate memory management in Java
public class MMExample {
    static class LinkedNode {
        int nodeData;
        LinkedNode nextNode;

        LinkedNode(int data) {
            this.nodeData = data;
        }
    }

    public static void main(String[] args) {
        System.out.println("Allocating new LinkedNode object");
        LinkedNode linkHead = new LinkedNode(20);
        linkHead.nextNode = new LinkedNode(500);

        System.out.println("linkHead data : " + linkHead.nodeData);
        System.out.println("nextNode data: " + linkHead.nextNode.nodeData);

        linkHead = null;
        System.gc();
        System.out.println("Completed!");
    }
}