/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spd;

import java.util.Observable;
import java.util.Observer;



/**
 *
 * @author 120323L
 */
/*creating the class cell. this class is a subsclass of observarable as well interface observer.
 * that is because the cells can have interconnections so there will be circumstances where
 * one cell should be observing the other
 */
class Cell extends Observable implements Observer 
{
    Spd sp1=new Spd();
    String index,textualVal,func,valueInString,eqn;
    Double value;
    char i,function;//i denotes the column
    int j;//j denotes the row
    /*creating the constructore cell. a cell is reffered to by its i aand j values
     * which denote its position in the spread sheet
     */
    public Cell(char i,int j)
    {
        this.i=i;
        this.j=j;
    }
    
    /*a method to set the index of a cell.*/
    public void setIndex(Cell sg)
    {
       
        /*assigning the location to a single string (index) so that 
        the location of a cellcan be used as a single varable*/
        this.index=Character.toString(sg.i)+Integer.toString(sg.j) ;
    }
    
    /*a method to set a value to a cell. 
    2 methods with the same name are created,one to set a double value and the other to set a string value*/
    
    public void setEqn(String eqn)
    {
        this.eqn=eqn;
    }
    
    
    public void setValue(Double val)
    {
        this.value = Math.round(val * 100.0) / 100.0;
        this.valueInString=Double.toString(this.value);//the value is assigned to a string in order to pass it to methodss like displayValue() and copy()

    }
    
    public void setValue(String val)
    {
        this.textualVal=val;
        this.valueInString=this.textualVal;

    }
    
    public void setValue(Cell X)
    {
        this.value=X.value;
        this.textualVal=X.textualVal;
        this.valueInString=X.valueInString;
    }
    
    public String getValue()
    {
        return this.valueInString;
    }
    
     public String getEqn()
    {
        return this.eqn;
    }

    /*following are methods to do the computations
    3 methods with the same name(compute) 
    are created to function according to the inputs given*/
    
    /*a method to do numerical computations. this method is employed in cases
    where the 2 operands and the operation are entered directly to a single cell*/ 
    public void compute(Double x,Double y,char function)
    {
        if(function=='+')
        {
            this.setValue(x+ y);
            this.valueInString=Double.toString(this.value);//the value is assigned to a string in order to simply pass it to the displayvalue function
            
        }
        
        if(function=='-')
        {
            this.setValue(x-y); 
            this.valueInString=Double.toString(this.value);//the value is assigned to a string in order to simply pass it to the displayvalue function
            
        }
        
         if(function=='*')
        {
            this.setValue(x* y);
            this.valueInString=Double.toString(this.value);//the value is assigned to a string in order to simply pass it to the displayvalue function
            
        }
         
         if(function=='/')
        {
             if(y==0)//if it is a devision by zero, the user is notified
                this.valueInString="#DIV/0!";
             else
             {
               this.setValue(x/y);
               this.valueInString=Double.toString(this.value);//the value is assigned to a string in order to simply pass it to the displayvalue function
             }
           
        }
    }
    /*a method to do the concatenation to 2 strings.
    here the 2 strings and the function are directly assigned to a single cell*/
    public void compute(String x,String y,String function)
    {
        if ("concatenate".equals(function))
        {
            this.valueInString=x+" "+y;
        }
    }
    /*a method to calculate the sum and the average of a range of cells(a row)
     * in this method, this cell is made an observer of all the cells to be calculated, so
     * that if the value in one of cells get changed, this cell would be notified.
     */
    public void compute(char i1, char i2,int j1,int j2,String function,Spd sp)
    {
        int cnt=0;
        double sum=0.0,max=sp.X[i1][j1].value,min=sp.X[i1][j1].value;
        String concSum ="";
       
        for(char k=i1;k<=i2;k++)
        {
            for(int l=j1;l<=j2;l++)
            {
                if(sp.X[k][l].value>max)
                    max=sp.X[k][l].value;
                if(sp.X[k][l].value<min)
                    min=sp.X[k][l].value;
            sp.X[k][l].addObserver(this);
            cnt+=1;
            concSum+=(sp.X[k][l].valueInString+" ");
            sum+=sp.X[k][l].value;
            }
        }
        switch (function) {
            case "sum":
                this.setValue(sum);     //set sum
                break;
            case "avg":
                this.setValue(sum/cnt); //set average
                break;
            case "min":
                this.setValue(min);     //set minimum
                break;
            case "conc":
                this.setValue(concSum);
                break;
            default:
                this.setValue(max);     //set maximum
                break;
        }
    }
  
  
    /*methods to displaay value*/
    
    /*the following method takes no inputs and 
    display only the final value in a cell.
    this method will be implemented when only the final valuue is needed to be displayed*/
    public void displayValue()
    {
       System.out.println("cell :"+this.index+"("+this.eqn+") \n= "+this.valueInString+"\n");
    }
    
    /*this displayValue method is implemented in cases where
    computations are done in between cells and the operation 
    and the 2 operand cells need to be displayed
    */
    public void displayValue(String res,String function,Cell x,Cell y)
    {
       System.out.println("cell :"+this.index+" \n= "+function+"("+x.i+""+x.j+","+y.i+""+y.j+") \n= "+res+"\n");
    }
    
    String copy(Cell x)
    {
        System.out.println(" cell copied!");
        return x.valueInString;//this method can copy a value of any type as valu is assigned to a string in both the the setValue methods 
    }
    
    String cut(Cell x)
    {
        String s= x.valueInString;
        x.textualVal=null;
        x.value=null;
        x.valueInString=null;
        System.out.println("cut operation done!");
        x.displayValue();
        return s;
    }
     public void paste(String valueinString)
     {
         this.valueInString=valueinString;
         System.out.println("paste operation done!");
         this.displayValue();
     }

    public void delete(Cell x)
    {
        System.out.println("cell "+x.index+" deleted");
        String s= x.valueInString;
        x.textualVal=null;
        x.value=null;
        x.valueInString=null;
        System.out.println("deleted!");
        x.displayValue();
    }

    @Override
    public void update(Observable o, Object arg)
    {
        this.getEntry(eqn,sp1);
        System.out.println("cell "+this.index+" updated!\n");
        
    }
/*a method to get the string equation and implement it
 * here, the string is first assigned to a char array and the array elements are observed to 
 * get the operands and the operations. then they are directed to suitable functions accordingly
 */
     public void getEntry(String eqn, Spd sp) 
     {
         sp1=sp;
            this.setEqn(eqn);//set the equation so that the equation could be summoned where necessary
            String s1,s2,s3,s4;
            double d1 = 0,d2 = 0,d3,d4,note=0,note1=0;
            long l1,l2;
            s1 = new String();
            char[] eqation=eqn.toCharArray();//getting the equation to a char array
            /*checking whether the second element of the array is a digit.
             * (first element is always '=')
             */
            if(Character.isDigit(eqn.charAt(1)))
            {
                for(int k=2;k<eqation.length;k++)/*if the second element is a digit check throgh the array ro 
                    find any operation if present*/
                {
                      if((eqation[k]=='+')||(eqation[k]=='-')||(eqation[k]=='*')||(eqation[k]=='/'))
                      {
                          note=1;//note if an operation is found
                          function=eqation[k];
                          s1 = new String(eqation, 1, k-1);//getting the first digit to a string
                          d1 = Double.parseDouble(s1);//getting the first digit to a double
                          if(Character.isDigit(eqn.charAt(k+1)))//if the elements after the operation is also a igit
                              //,get that digit toa double and calling the method to do the computation
                          {
                                   s2 = new String(eqation,k+1,eqation.length-(k+1));
                                   d2 = Double.parseDouble(s2); 
                                   this.compute(d1,d2,function);
                          }
                          else//if the element after the operation is not a digit, it must be a cell index.
                              //get the cell value and call the method to do the computation
                          {
                              int j2=eqation[eqation.length-1]-48;//System.out.println(eqn.charAt(k+1));
                              this.compute(d1,sp.X[eqn.charAt(k+1)][j2].value, function);
                          }
                           
                      }
                }
                if(note==0)//if an operation was not found after the first digit,set the first digit as the value of the cell
                {
                    s3 = new String(eqation, 1,eqation.length-1);
                    d3 = Double.parseDouble(s3);
                    this.setValue(d3);
                }
            } 
            /*if the second element is not a digit but the third element is a digit, 
             * it must refer to a cell index
             */
            else if(Character.isDigit(eqn.charAt(2)))
            {
                int j2=eqation[2]-48;//get the charactor of the jth value of the cell to an integer
                for(int k=2;k<eqation.length;k++)/*if the second element is a cell check throgh the array ro 
                    find any operation if present*/
                {
                    if((eqation[k]=='+')||(eqation[k]=='-')||(eqation[k]=='*')||(eqation[k]=='/')||(eqation[k]==':'))
                    {
                         note1=1;//note if an operation is found
                         function=eqation[k];
                         if(Character.isDigit(eqn.charAt(k+1)))/*if the element after the operation is a digit,
                          * get its value to a double and call the method to do the computations
                          */
                         {
                             s4 = new String(eqation,k+1,eqation.length-(k+1));
                             d4 = Double.parseDouble(s4);
                             this.compute(sp.X[eqn.charAt(1)][j2].value,d4,function);
                         }
                         else if(eqation[k]==':')//if the operation is ":", it refers to the sum of a range of cells.
                             /*the first cell is the starting cell and the cell after the operation is the ending cell
                              * call the method to get the sum of this range
                              */
                         {
                             int j1=eqation[5]-48; //System.out.println(eqation[1]);
                             this.compute(eqation[1],eqation[4],j2,j1,"sum", sp);
                         }
                         /*if not for a digit, the elements afre the operation must be anothe cell.
                          * get the value of that cell and call method for the given computation
                          */
                         else
                         {
                           int j1=eqation[5]-48; 
                           this.compute(sp.X[eqation[1]][j2].value,sp.X[eqation[4]][j1].value,function);
                         }
                    }
                }
                if(note1==0)/*if any operation was not noted, assign the value of the
                 * first cell to the value of this cell
                 */
                this.setValue(sp.X[eqation[1]][j2]);   
            }
            /*if the eqation contains "avg", call the function to get the average*/
            else if(eqn.contains("avg("))
            {
                //System.out.println(eqation[5]+" "+eqation[8]);
                this.compute(eqation[5],eqation[8],eqation[6]-48,eqation[9]-48,"avg", sp);
            }
            
            /*if the eqation contains "min", call the function to get the minimum*/
            else if(eqn.contains("min("))
            {
                //System.out.println(eqation[5]+" "+eqation[8]);
                this.compute(eqation[5],eqation[8],eqation[6]-48,eqation[9]-48,"min", sp);
            }
            
             /*if the eqation contains "conc(", call the function to get the concatenation*/
            else if(eqn.contains("conc("))
            {
                //System.out.println(eqation[5]+" "+eqation[8]);
                this.compute(eqation[6],eqation[9],eqation[7]-48,eqation[10]-48,"conc", sp);
            }
            
             /*if the eqation contains "max", call the function to get the maximum*/
            else if(eqn.contains("max("))
            {
                //System.out.println(eqation[5]+" "+eqation[8]);
                this.compute(eqation[5],eqation[8],eqation[6]-48,eqation[9]-48,"max", sp);
            }
          /*if none of the above conditions are satisfied, the eqatin passed must bee a mere string value 
            * assign that string as the textual value of the cell
           */
            else
            {
                s1= new String(eqation,1,eqation.length-1);
                this.setValue(s1);
            }
            
            setChanged();           
            //notify observers when content of an observable is changed
            notifyObservers();  
     }
     
 
}


public class Spd
{
    Cell[][] X=new Cell[300][300];
    private void setRange(char i1, char i2,int j1,int j2)
    {
        for(char i=i1;i<=i2;i++)
        {
            for(int j=j1;j<=j2;j++)
            {
                X[i][j]=new Cell(i,j);
                X[i][j].setIndex(X[i][j]);
                X[i][j].valueInString="()";
                X[i][j].eqn="";
                X[i][j].value=0.0;
                X[i][j].textualVal="";
            }
        }
        
    }
//a method to display a selected part of the sheet(column)
    private void displaySheet(char i1, char i2,int j1,int j2)
    {
         for(char i=i1;i<=i2;i++)
            {
                System.out.print("\t"+i+"\t");
            }
        
        for(int j=j1;j<=j2;j++)
        {
            System.out.print("\n"+j+"\t");
            for(char i=i1;i<=i2;i++)
            {
                if(X[i][j].valueInString.length()<6)
                    System.out.print(X[i][j].valueInString+"\t\t");
                else
                    System.out.print(X[i][j].valueInString+"\t");    
            }
            System.out.print("\n\t");
            for(char i=i1;i<=i2;i++)
            {
                if(X[i][j].eqn.length()<6)
                    System.out.print("("+X[i][j].eqn+")\t\t");
                else
                    System.out.print("("+X[i][j].eqn+")\t");
       
            }
        }
    }

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        // TODO code application logic here
        Spd sp=new Spd();
        sp.setRange('A','E',0,15);
        /*demonstrate different methods associated with a column of cells*/
        /*the values of number of cells fepends on the value of A1.
         * therefore those cells are made observers of A1.next change the vaue of A1 and display 
         * the updated sheet to demonstrate the changes of the observers according to the change in A1
         */
        sp.X['A'][0].getEntry("=10.00*2",sp);
        sp.X['A'][1].getEntry("=8.2",sp);
        sp.X['A'][1].addObserver(sp.X['A'][2]);
        sp.X['A'][2].getEntry("=12.0+A1",sp);
        sp.X['A'][1].addObserver(sp.X['B'][0]);
        sp.X['B'][0].getEntry("=A1",sp);
        sp.X['A'][1].addObserver(sp.X['B'][1]);
        sp.X['B'][0].addObserver(sp.X['B'][1]);
        sp.X['B'][1].getEntry("=B0+A1",sp);
        sp.X['A'][1].addObserver(sp.X['B'][2]);
        sp.X['B'][2].getEntry("=A1-2",sp);
        sp.X['A'][3].getEntry("=A0:B2",sp);
        sp.X['B'][3].getEntry("=avg(A0:B2)",sp);
        sp.X['A'][4].getEntry("=min(A0:B2)",sp);
        sp.X['B'][4].getEntry("=max(A0:B2)",sp);
        sp.X['D'][3].getEntry("=uom",sp);
        sp.X['E'][3].getEntry("=the",sp);
        sp.X['D'][4].getEntry("=cse",sp);
        sp.X['E'][4].getEntry("=best",sp);
        sp.X['D'][5].getEntry("=is",sp);
        sp.X['E'][5].getEntry("=forever",sp);
        sp.X['E'][6].getEntry("=conc(D3:E5)",sp);
        
        System.out.println("Spreadsheet");
        sp.displaySheet('A','E',0,6);//display the sheet(column)
        System.out.println("\nchange tha value of cell A1");
        sp.X['A'][1].getEntry("=23.0",sp);//change the value of a cell 
        sp.X['A'][1].displayValue();//display the new value of A1
        System.out.println("updated sheet");
        sp.displaySheet('A','E',0,6);//display the sheet after update
        }
    
}
