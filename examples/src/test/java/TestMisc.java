import org.junit.Test;
import picturesolver.DnaDrawerPolys;

public class TestMisc {

    @Test
    public void t1() {
        DnaDrawerPolys dnaDrawerPolys = new DnaDrawerPolys();

        System.out.println(dnaDrawerPolys.calculatePositionPenalty(-0.5,1));
        System.out.println(dnaDrawerPolys.calculatePositionPenalty(0,1));
        System.out.println(dnaDrawerPolys.calculatePositionPenalty(1.5,1));
    }


}
