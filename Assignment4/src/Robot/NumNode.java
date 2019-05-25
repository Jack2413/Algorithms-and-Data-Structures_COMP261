package Robot;

public class NumNode implements EXPNode{

    private final int num;

    public NumNode(int num) {
        this.num = num;
    }

    
    public int execute(Robot robot) {
        return this.num;
    }

    @Override
    public String toString() {
        return "" + num;
    }

}

