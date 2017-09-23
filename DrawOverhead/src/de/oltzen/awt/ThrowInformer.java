/* (c) by Thomas Oltzen
 * Created on 07.01.2005
 *
 */
package de.oltzen.awt;

import java.util.Vector;

/**
 * @author Admin
 *
 */
public class ThrowInformer {

    private static Vector<ThrowListener> theList = new Vector<ThrowListener>();
    
    public static void inform(Throwable texc)
    {
        texc.printStackTrace();
        for (int i = 0 ; i< theList.size() ; i++){
            theList.get(i).informException(texc);
        }
    }
    
    public static void addThrowListener(ThrowListener tl)
    {
        theList.add(tl);
    }
}
