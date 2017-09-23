/* 
 * (C) Copyright 2017, by Thomas Oltzen.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
*/
package de.oltzen.awt;

import java.awt.*;

/** A real simple layout manager for your program. <br>
* <b>1. Example:</b>
* <pre>
* Container cont;
* TableLayout tl = new TableLayout(4,3);
* cont.setLayout(tl);
* tl.setRowRel(1,25);
* tl.setRowRel(2,75);
* Label la = new Label("Test1");
* Label lb = new Label("Test2");
* Label lc = new Label("Test3");
* count.add(la); count.add(lb); count.add(lc);
* tl.addComponent(la,0,0);
* tl.addComponent(lb,1,1,2,1);
* tl.addComponent(lb,3,2,1,2);
* </pre>
* 
* <br>
* <b>2. Example:</b>
* <pre>
* Container cont;
* TableLayout tl = new TableLayout(4,3);
* cont.setLayout(tl);
* tl.setRowRel(1,25);
* tl.setRowRel(2,75);
* Label la = new Label("Test1");
* Label lb = new Label("Test2");
* Label lc = new Label("Test3");
* tl.addComponentNewLine(cont, la);
* tl.addComponentNewLine(cont, lb);
* tl.addComponentNewLine(cont, lc);
* </pre>
* <b>&copy; 1999 by Thomas Oltzen under the terms of the LGPL</b>
* @version 1.01
* @author Thomas Oltzen
*/
public class TableLayout implements LayoutManager
{

	//private int theRowCounter;

	//private int theColCounter;

	/** Created all with 20. */
	private int theRowHeight[];

	/** Created all with 0. */
	private int theRowHeightRel[];

	/** Created all with 80. */
	private int theColWidth[];

	/** Created all with 0. */
	private int theColWidthRel[];

	/** Created all with 1. */
	private int theBorderLineRowHeight [];

	/** Created all with 1. */
	private int theBorderLineColWidth [];

	private java.util.Hashtable<Component,TabelElement> theCompToTableElement;
	
	private int theAddPosRow=-1;

	private int theAddPosCol;	

	/** Create a new layout. Note the row and column counter can not
	* be change!
	*/
	public TableLayout(int row_counter, int col_counter)
	{
		//theRowCounter = row_counter;
		//theColCounter = col_counter;
		theBorderLineRowHeight = new int [row_counter+1];
		theBorderLineColWidth = new int [col_counter+1];
		theRowHeight = new int [row_counter];
		theRowHeightRel = new int [row_counter];
		for (int i = 0 ; i < row_counter ; i ++)
		{
			theRowHeight[i]=20;
			theBorderLineRowHeight[i] = 1;
		}
		theBorderLineRowHeight[row_counter]=1;
		theColWidth = new int [col_counter];
		theColWidthRel = new int [col_counter];
		for (int i = 0 ; i < col_counter ; i ++)
		{
			theColWidth[i]=80;
			theBorderLineColWidth[i] = 1;
		}
		theBorderLineColWidth[col_counter]=1;
		theCompToTableElement = new java.util.Hashtable<Component,TabelElement>();
	}

	/** Insert a new component into the layout. The left-top component is at row=0 and
	* col=0. This method do not test the range of row and col!
	*/
	public void addComponent(Component comp, int row, int col)
	{
		TabelElement ele = new TabelElement();
		ele.theComponent = comp;
		ele.theRow = row;
		ele.theCol = col;
		theCompToTableElement.put(comp, ele);
		theAddPosRow = row;
		theAddPosCol = col;
	}

	/** Insert a new component into the layout. The left-top component is at row=0 and
	* col=0. This method do not test the range of row and col!
	*/
	public void addComponent(Component comp, int row, int col, int rjoint, int cjoint)
	{
		TabelElement ele = new TabelElement();
		ele.theComponent = comp;
		ele.theRow = row;
		ele.theCol = col;
		ele.theRowJoint = rjoint-1;
		ele.theColJoint = cjoint-1;
		theCompToTableElement.put(comp, ele);
		theAddPosRow = row+rjoint-1;
		theAddPosCol = col+cjoint-1;
	}
    
    public void setPrefferedSize(Component comp, boolean flag)
    {
        TabelElement ele = (TabelElement) theCompToTableElement.get(comp);
        if (ele != null){
            ele.thePrefSize = flag;
        } else {
            System.err.println("WARNING! The setPrefferedSize must set after the addComponent.");
        }
    }

    public void setPrefferedSize(Component comp, int width, int height)
    {
        TabelElement ele = (TabelElement) theCompToTableElement.get(comp);
        if (ele != null){
            ele.thePrefWidth = width;
            ele.thePrefHeight = height;
        } else {
            System.err.println("WARNING! The setPrefferedSize must set after the addComponent.");
        }
    }
	/** Insert an new component in the same row, and the right to the last component. 
	 * The parameter <b>con<b> can be 'null'.
	 * @param con Container gets the component.
	 * @param comp
	 */
	public void addComponentSameRow(Container con, Component comp)
	{
		theAddPosCol ++;
		TabelElement ele = new TabelElement();
		ele.theComponent = comp;
		ele.theRow = theAddPosRow;
		ele.theCol = theAddPosCol;
		theCompToTableElement.put(comp, ele);
		if (con != null) con.add(comp);
	}

	/** Insert an new component in the next row and first column. 
	 * The parameter <b>con<b> can be 'null'.
	 * 
	 * @param con Container gets the component.
	 * @param comp
	 */
	public void addComponentNewLine(Container con, Component comp)
	{
		theAddPosRow ++;
		theAddPosCol = 0;
		TabelElement ele = new TabelElement();
		ele.theComponent = comp;
		ele.theRow = theAddPosRow;
		ele.theCol = theAddPosCol;
		theCompToTableElement.put(comp, ele);
		if (con != null) con.add(comp);
	}

	/** The gap between two rows are normal 0. Here can you set another value.
	*/
	public void setRowLine(int nr, int width)
	{
		theBorderLineRowHeight[nr] = width;
	}

	/** The gap between two colums are normal 0. Here can you set another value.
	*/
	public void setColLine(int nr, int height)
	{
		theBorderLineColWidth[nr] = height;
	}

	/** Set the static/minimum height of a row. */
	public void setRowHeight(int nr, int height)
	{
		theRowHeight[nr] = height;
	}

	/** Set the static/minimum width of a colume. */
	public void setColWidth(int nr, int width)
	{
		theColWidth[nr] = width;
	}

	/** Set the relative value from a row. This method has much power.
	* Initialy has every row a static height. With this method can you
	* set the variation of a row. The static height is the minimum height. <br>
	* Supposing that we have four rows. All rows have the static/minimum height of 20.
	* The call <code>tl.setRowRel(2,30)</code> set the <b>3rd</b> row with the
	* variation of 30. Has the container a height of 200, than is the height of 3rd row
	* 140 pixels. How that? The sum of all static/minimum height of row is 80. The rest is
	* 120 pixels. Now, the height of 3rd row is add of the minimum height of 20 Pixels and the rest
	* of 120 pixels. This is 140 pixels. <br>
	* What is if a <code>tl.setRowRel(3,90)</code> called? The height of 3rd row is 50 and 4th
	* row is 70. <br>
	* <pre>
	*                                                        (120 pixels rest)*(30 the 3rd row variation)
	*       3rd row height = (20 pixels minimum height)+-----------------------------------------------------
	*                                                   (30 the 3rd row variation)+(40 the 4rd row variation)
	* </pre>
	* The variation is relative to all other row variations!
	*/
	public void setRowRel(int nr, int height)
	{
		theRowHeightRel[nr] = height;
	}

	/** See <code>setRowRel</code>. */
	public void setColRel(int nr, int width)
	{
		theColWidthRel[nr] = width;
	}


	/** Adds the specified component with the specified name to the layout.
    * @param name the component name
    * @param comp the component to be added
	*/
  	public void  addLayoutComponent(String name, Component comp)
	{
		System.err.println( "Diese Methode TableLayout::addLayoutComponent macht "+
							"nichts. Es soll die Methode addComponent aufgerufen "+
							" werden.");
	}


    /** Lays out the container in the specified panel.
	*/
	public void layoutContainer(Container parent)
	{
      	synchronized (parent.getTreeLock())
		{
			Insets insets = parent.getInsets();
			int ncomponents = parent.getComponentCount();
			//int nrows = theRowCounter;
			//int ncols = theColCounter;

			if (ncomponents == 0) { return; }

			int w = parent.getWidth() - (insets.left + insets.right);
			int h = parent.getHeight() - (insets.top + insets.bottom);

			int rowmin = calculateMin( theRowHeight, theBorderLineRowHeight);
			int colmin = calculateMin( theColWidth, theBorderLineColWidth);

			int [] height = calculateWidth(theRowHeightRel, theRowHeight, h-rowmin);
			int [] width = calculateWidth(theColWidthRel, theColWidth, w-colmin);

			int [] xpos = calculatePos(width, theBorderLineColWidth);
			int [] ypos = calculatePos(height, theBorderLineRowHeight);

			for (int i = 0 ; i < ncomponents ; i++)
			{
				TabelElement ele =
					(TabelElement) theCompToTableElement.get(parent.getComponent(i));
				if (ele != null)
				{
					ele.setPosition
					(
						xpos,
						ypos,
						width,
						height,
						insets.left,
						insets.top
					);
				}
			}
      	}
    }

	/** Calculates the minimum size dimensions for the specified panel given
    * the components in the specified parent container.
  	*/
	public Dimension minimumLayoutSize(Container parent)
	{
      	synchronized (parent.getTreeLock())
		{
			//System.out.println("minimum for "+parent);
        	Insets insets = parent.getInsets();

			//int ncomponents = parent.getComponentCount();

			int rowmin = calculateMin( theRowHeight, theBorderLineRowHeight);
			int colmin = calculateMin( theColWidth, theBorderLineColWidth);

			return new Dimension
				(
					insets.left + insets.right + colmin,
			     	insets.top + insets.bottom + rowmin
				);
      	}
    }

   	/** Calculates the preferred size dimensions for the specified panel given
    * the components in the specified parent container.
	*/
  	public Dimension preferredLayoutSize(Container parent)
	{
      	return minimumLayoutSize(parent);
    }

	private int[] calculatePos(int width[], int line_width[])
	{
		int [] output = new int [width.length];
		if (width.length>0)
		{
			output[0] = line_width[0];
			for (int i = 1; i < width.length ; i ++)
			{
				output[i]=output[i-1]+line_width[i]+width[i-1];
			}
		}
		return output;
	}

	/** Berechnet die Breite der Zeilen und Spalten. */
	private int[] calculateWidth(int [] width_rel, int [] width, int width_rest)
	{
		int [] output = new int [width_rel.length];
		int width_rel_sum = 0 ;
		int last_rel_index = 0;
		int real_width_rest = width_rest;
		for (int i = 0 ; i < width_rel.length ; i ++)
		{
			width_rel_sum = width_rel_sum + width_rel[i];
			if (width_rel[i] > 0)
			{
				last_rel_index = i;
			}
		}
		if (width_rel_sum > 0 && real_width_rest > 0)
		{
			int width_rel_sum_assert = 0;
			for (int i = 0 ; i < width_rel.length ; i ++)
			{
				output[i]=width_rel[i]*width_rest/width_rel_sum;
				width_rel_sum_assert += output[i];
			}
			output[last_rel_index]+=(width_rest-width_rel_sum_assert);
		}
		for (int i = 0 ; i < width_rel.length ; i ++)
		{
			output[i]+=width[i];
		}
		return output;
	}
	
	public int getMinimalHeight()
	{
	    return calculateMin( theRowHeight, theBorderLineRowHeight);
	}

	private int calculateMin( int[] width, int[] line_width )
	{
		int minsize = 0;
		for (int i = 0 ; i < width.length ; i ++)
		{
			minsize = minsize+width[i];
			minsize = minsize+line_width[i];
		}
		minsize = minsize + line_width[width.length];
		return minsize;
	}

	/** Removes the specified component from the layout.
	*/
	public void removeLayoutComponent(Component comp)
	{
		TabelElement ele = theCompToTableElement.get(comp);
		if (ele != null)
		{
			theCompToTableElement.remove(comp);
		}
	}

	class TabelElement
	{
		Component theComponent;
		int theRow;
		int theCol;

		/** Anzahl der Felder die Zusammengesetzt werden minus 1.
		* Dies bedeutet, daß theRowJoint=2 drei Felder zusammenfast.
		*/
		int theRowJoint;
		int theColJoint;
        
        boolean thePrefSize;
        int thePrefWidth;
        int thePrefHeight;

		void setPosition
		(
			int [] xpos,
			int [] ypos,
			int [] width,
			int [] height,
			int left,
			int top
		)
		{
			try
			{
				int w = width[theCol+theColJoint]+
						xpos[theCol+theColJoint]-xpos[theCol];
				int h = height[theRow+theRowJoint]+
						ypos[theRow+theRowJoint]-ypos[theRow];
                
                int xd = 0;
                int yd = 0; 
                if (thePrefWidth!=0 && thePrefHeight!=0){
                    if (thePrefHeight<h){
                        yd = (h - thePrefHeight)/2;
                        h = thePrefHeight;
                    }
                    if (thePrefWidth<w){
                        xd = (w - thePrefWidth)/2;
                        w = thePrefWidth;
                    }
                } else if (thePrefSize){
                    Dimension d = theComponent.getPreferredSize();
                    
                    if (d.height<h){
                        yd = (h - d.height)/2;
                        h = d.height;
                    }
                    if (d.width<w){
                        xd = (w - d.width)/2;
                        w = d.width;
                    }
                }
				//System.out.println
				//	(""+theComponent+" "
				//     +xpos[theRow]+","+ypos[theCol]+","+w+","+h);
				theComponent.setBounds(xpos[theCol]+xd, ypos[theRow]+yd, w, h);
			}
			catch (java.lang.ArrayIndexOutOfBoundsException exc)
			{
                ThrowInformer.inform(exc);
				System.err.println("Component="+theComponent);
				System.err.println("xpos.length="+xpos.length+
								   " ypos.length="+ypos.length);
				System.err.println("Row="+ theRow+ " Col="+ theCol);
				System.err.println("RowJoint="+ theRowJoint+
				                   " ColJoint="+ theColJoint);
			}
		}
	}



}