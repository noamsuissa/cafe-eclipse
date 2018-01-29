/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/


import java.util.*;

// line 57 "iteration1.ump"
public class Seat
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Seat Attributes
  private int seatNumber;

  //Seat Associations
  private Table table;
  private Bill bill;
  private List<OrderItem> orderItems;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Seat(int aSeatNumber, Table aTable, Bill aBill)
  {
    seatNumber = aSeatNumber;
    boolean didAddTable = setTable(aTable);
    if (!didAddTable)
    {
      throw new RuntimeException("Unable to create seat due to table");
    }
    boolean didAddBill = setBill(aBill);
    if (!didAddBill)
    {
      throw new RuntimeException("Unable to create seat due to bill");
    }
    orderItems = new ArrayList<OrderItem>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setSeatNumber(int aSeatNumber)
  {
    boolean wasSet = false;
    seatNumber = aSeatNumber;
    wasSet = true;
    return wasSet;
  }

  public int getSeatNumber()
  {
    return seatNumber;
  }

  public Table getTable()
  {
    return table;
  }

  public Bill getBill()
  {
    return bill;
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

  public boolean setBill(Bill aBill)
  {
    boolean wasSet = false;
    //Must provide bill to seat
    if (aBill == null)
    {
      return wasSet;
    }

    if (bill != null && bill.numberOfSeats() <= Bill.minimumNumberOfSeats())
    {
      return wasSet;
    }

    Bill existingBill = bill;
    bill = aBill;
    if (existingBill != null && !existingBill.equals(aBill))
    {
      boolean didRemove = existingBill.removeSeat(this);
      if (!didRemove)
      {
        bill = existingBill;
        return wasSet;
      }
    }
    bill.addSeat(this);
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

  public void delete()
  {
    Table placeholderTable = table;
    this.table = null;
    if(placeholderTable != null)
    {
      placeholderTable.removeSeat(this);
    }
    Bill placeholderBill = bill;
    this.bill = null;
    if(placeholderBill != null)
    {
      placeholderBill.removeSeat(this);
    }
    ArrayList<OrderItem> copyOfOrderItems = new ArrayList<OrderItem>(orderItems);
    orderItems.clear();
    for(OrderItem aOrderItem : copyOfOrderItems)
    {
      aOrderItem.removeSeat(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "seatNumber" + ":" + getSeatNumber()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "table = "+(getTable()!=null?Integer.toHexString(System.identityHashCode(getTable())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "bill = "+(getBill()!=null?Integer.toHexString(System.identityHashCode(getBill())):"null");
  }
}