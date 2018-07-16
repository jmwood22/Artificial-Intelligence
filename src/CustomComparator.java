import java.util.Comparator;

public class CustomComparator implements Comparator<EndpointPair> 
{
    @Override
    public int compare(EndpointPair o1, EndpointPair o2) 
    {
        return o1.getdist() - o2.getdist();
    }
}
