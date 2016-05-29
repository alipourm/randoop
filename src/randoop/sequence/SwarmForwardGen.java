package randoop.sequence;

import edu.emory.mathcs.backport.java.util.Collections;
import randoop.ComponentManager;
import randoop.IStopper;
import randoop.ITestFilter;
import randoop.RandoopListenerManager;
import randoop.operation.Operation;
import randoop.util.Log;

import java.util.ArrayList;

import java.util.List;

/**
 * Created by alipourm on 5/29/16.
 */
public class SwarmForwardGen extends ForwardGenerator {
    List<Operation> localstatements;
    public SwarmForwardGen(List<Operation> statements, long timeMillis, int maxSequences, ComponentManager componentManager, IStopper stopper, RandoopListenerManager listenerManager, List<ITestFilter> fs) {
        super(statements, timeMillis, maxSequences, componentManager, stopper, listenerManager, fs);
        localstatements.addAll(statements);
    }

    @Override
    public void explore(){
        ArrayList tempList = new ArrayList();
        Collections.shuffle(localstatements);
        for (int i = 0; i <= localstatements.size()/2 ; i++)
            tempList.add(localstatements.get(i));
        statements = tempList;
        Log.log("Swarm Step");
        super.explore();

    }
}
