import org.junit.Test;

/**
 * @author ：DYZ
 * @date ：Created By 2020/12/12 16:36
 */
public class test1 {
    @Test
    public void run1()
    {
        String name=null,address="address",email=null;

        String otherParam="?";
        if(name!=null||address!=null||email!=null)
        {
            otherParam=(name!=null)?(otherParam+="name="+name+"&"):otherParam;
            otherParam=(address!=null)?(otherParam+="address="+address+"&"):otherParam;
            otherParam=(email!=null)?(otherParam+="email="+email+"&"):otherParam;
        }
        System.out.println(otherParam);
    }

}
