/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.26.1-f40f105-3613 modeling language!*/

package ca.mcgill.ecse223.resto.model;
import java.io.Serializable;
import java.util.*;

// line 94 "../../../../../RestoAppPersistence.ump"
// line 57 "../../../../../RestoApp v3.ump"
public class Seat implements Serializable
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Autounique Attributes
  private int Id;

  //Seat Associations
  private Table table;
  private List<OrderItem> orderItems;
  private List<Bill> bills;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Seat(Table aTable)
  {
    Id = nextId++;
    boolean didAddTable = setTable(aTable);
    if (!didAddTable)
    {
      throw new RuntimeException("Unable to create seat due to table");
    }
    orderItems = new ArrayList<OrderItem>();
    bills = new ArrayList<Bill>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public int getId()
  {
    return Id;
  }

  public Table getTable()
  {
    return table;
  }

  public OrderItem getOrderItem(int index)
  {
    OrderItem aOrderItem = orderItems.get(index);
    return aOrderItem;
  }

  public List<OrderItem> getOrderItems()
  {
    List<OrderItem> newOrderItems = Collections.unmodifiableList(orderItems);
    return newOrderItems;
  }

  public int numberOfOrderItems()
  {
    int number = orderItems.size();
    return number;
  }

  public boolean hasOrderItems()
  {
    boolean has = orderItems.size() > 0;
    return has;
  }

  public int indexOfOrderItem(OrderItem aOrderItem)
  {
    int index = orderItems.indexOf(aOrderItem);
    return index;
  }

  public Bill getBill(int index)
  {
    Bill aBill = bills.get(index);
    return aBill;
  }

  public List<Bill> getBills()
  {
    List<Bill> newBills = Collections.unmodifiableList(bills);
    return newBills;
  }

  public int numberOfBills()
  {
    int number = bills.size();
    return number;
  }

  public boolean hasBills()
  {
    boolean has = bills.size() > 0;
    return has;
  }

  public int indexOfBill(Bill aBill)
  {
    int index = bills.indexOf(aBill);
    return index;
  }

  public boolean setTable(Table aTable)
  {
    boolean wasSet = false;
    //Must provide table to seat
    if (aTable == null)
    {
      return wasSet;
    }

    if (table != null && table.numberOfSeats() <= Table.minimumNumberOfSeats())
    {
      return wasSet;
    }

    Table existingTable = table;
    table = aTable;
    if (existingTable != null && !existingTable.equals(aTable))
    {
      boolean didRemove = existingTable.removeSeat(this);
      if (!didRemove)
      {
        table = existingTable;
        return wasSet;
      }
    }
    table.addSeat(this);
    wasSet = true;
    return wasSet;
  }

  public static int minimumNumberOfOrderItems()
  {
    return 0;
  }

  public boolean addOrderItem(OrderItem aOrderItem)
  {
    boolean wasAdded = false;
    if (orderItems.contains(aOrderItem)) { return false; }
    orderItems.add(aOrderItem);
    if (aOrderItem.indexOfSeat(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aOrderItem.addSeat(this);
      if (!wasAdded)
      {
        orderItems.remove(aOrderItem);
      }
    }
    return wasAdded;
  }

  public boolean removeOrderItem(OrderItem aOrderItem)
  {
    boolean wasRemoved = false;
    if (!orderItems.contains(aOrderItem))
    {
      return wasRemoved;
    }

    int oldIndex = orderItems.indexOf(aOrderItem);
    orderItems.remove(oldIndex);
    if (aOrderItem.indexOfSeat(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aOrderItem.removeSeat(this);
      if (!wasRemoved)
      {
        orderItems.add(oldIndex,aOrderItem);
      }
    }
    return wasRemoved;
  }

  public boolean addOrderItemAt(OrderItem aOrderItem, int index)
  {  
    boolean wasAdded = false;
    if(addOrderItem(aOrderItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderItems()) { index = numberOfOrderItems() - 1; }
      orderItems.remove(aOrderItem);
      orderItems.add(index, aOrderItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderItemAt(OrderItem aOrderItem, int index)
  {
    boolean wasAdded = false;
    if(orderItems.contains(aOrderItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderItems()) { index = numberOfOrderItems() - 1; }
      orderItems.remove(aOrderItem);
      orderItems.add(index, aOrderItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderItemAt(aOrderItem, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfBills()
  {
    return 0;
  }

  public boolean addBill(Bill aBill)
  {
    boolean wasAdded = false;
    if (bills.contains(aBill)) { return false; }
    bills.add(aBill);
    if (aBill.indexOfIssuedForSeat(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aBill.addIssuedForSeat(this);
      if (!wasAdded)
      {
        bills.remove(aBill);
      }
    }
    return wasAdded;
  }

  public boolean removeBill(Bill aBill)
  {
    boolean wasRemoved = false;
    if (!bills.contains(aBill))
    {
      return wasRemoved;
    }

    int oldIndex = bills.indexOf(aBill);
    bills.remove(oldIndex);
    if (aBill.indexOfIssuedForSeat(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aBill.removeIssuedForSeat(this);
      if (!wasRemoved)
      {
        bills.add(oldIndex,aBill);
      }
    }
    return wasRemoved;
  }

  public boolean addBillAt(Bill aBill, int index)
  {  
    boolean wasAdded = false;
    if(addBill(aBill))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBills()) { index = numberOfBills() - 1; }
      bills.remove(aBill);
      bills.add(index, aBill);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveBillAt(Bill aBill, int index)
  {
    boolean wasAdded = false;
    if(bills.contains(aBill))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBills()) { index = numberOfBills() - 1; }
      bills.remove(aBill);
      bills.add(index, aBill);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addBillAt(aBill, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Table placeholderTable = table;
    this.table = null;
    placeholderTable.removeSeat(this);
    ArrayList<OrderItem> copyOfOrderItems = new ArrayList<OrderItem>(orderItems);
    orderItems.clear();
    for(OrderItem aOrderItem : copyOfOrderItems)
    {
      if (aOrderItem.numberOfSeats() <= OrderItem.minimumNumberOfSeats())
      {
        aOrderItem.delete();
      }
      else
      {
        aOrderItem.removeSeat(this);
      }
    }
    ArrayList<Bill> copyOfBills = new ArrayList<Bill>(bills);
    bills.clear();
    for(Bill aBill : copyOfBills)
    {
      if (aBill.numberOfIssuedForSeats() <= Bill.minimumNumberOfIssuedForSeats())
      {
        aBill.delete();
      }
      else
      {
        aBill.removeIssuedForSeat(this);
      }
    }
  }

  // line 98 "../../../../../RestoAppPersistence.ump"
   public static  void reinitializeAutoUniqueId(List<Table> tables){
    for(Table table : tables){
  		List<Seat> seats = table.getSeats();
  		for(Seat seat : seats){
  			if(seat.getId() > nextId){
  				nextId = seat.getId();
  			}
  		}
  		nextId++;
  	}
  }


  public String toString()
  {
    return super.toString() + "["+
            "Id" + ":" + getId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "table = "+(getTable()!=null?Integer.toHexString(System.identityHashCode(getTable())):"null");
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 108 ../../../../../RestoAppPersistence.ump
  private static final long serialVersionUID = 386717977557499839L ;

  
}