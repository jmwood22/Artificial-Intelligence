import java.util.Comparator;

public class CustomComparator2 implements Comparator<NumbrixMove> 
{
    @Override
    public int compare(NumbrixMove o1, NumbrixMove o2) 
    {
        return o1.getVal() - o2.getVal();
    }
}