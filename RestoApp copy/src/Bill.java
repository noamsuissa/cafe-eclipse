/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/


import java.util.*;

// line 43 "iteration1.ump"
public class Bill
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Bill Attributes
  private int total;

  //Bill Associations
  private List<Seat> seats;
  private List<Table> tables;
  private List<Order> orders;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Bill(int aTotal)
  {
    total = aTotal;
    seats = new ArrayList<Seat>();
    tables = new ArrayList<Table>();
    orders = new ArrayList<Order>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTotal(int aTotal)
  {
    boolean wasSet = false;
    total = aTotal;
    wasSet = true;
    return wasSet;
  }

  public int getTotal()
  {
    return total;
  }

  public Seat getSeat(int index)
  {
    Seat aSeat = seats.get(index);
    return aSeat;
  }

  public List<Seat> getSeats()
  {
    List<Seat> newSeats = Collections.unmodifiableList(seats);
    return newSeats;
  }

  public int numberOfSeats()
  {
    int number = seats.size();
    return number;
  }

  public boolean hasSeats()
  {
    boolean has = seats.size() > 0;
    return has;
  }

  public int indexOfSeat(Seat aSeat)
  {
    int index = seats.indexOf(aSeat);
    return index;
  }

  public Table getTable(int index)
  {
    Table aTable = tables.get(index);
    return aTable;
  }

  public List<Table> getTables()
  {
    List<Table> newTables = Collections.unmodifiableList(tables);
    return newTables;
  }

  public int numberOfTables()
  {
    int number = tables.size();
    return number;
  }

  public boolean hasTables()
  {
    boolean has = tables.size() > 0;
    return has;
  }

  public int indexOfTable(Table aTable)
  {
    int index = tables.indexOf(aTable);
    return index;
  }

  public Order getOrder(int index)
  {
    Order aOrder = orders.get(index);
    return aOrder;
  }

  public List<Order> getOrders()
  {
    List<Order> newOrders = Collections.unmodifiableList(orders);
    return newOrders;
  }

  public int numberOfOrders()
  {
    int number = orders.size();
    return number;
  }

  public boolean hasOrders()
  {
    boolean has = orders.size() > 0;
    return has;
  }

  public int indexOfOrder(Order aOrder)
  {
    int index = orders.indexOf(aOrder);
    return index;
  }

  public boolean isNumberOfSeatsValid()
  {
    boolean isValid = numberOfSeats() >= minimumNumberOfSeats();
    return isValid;
  }

  public static int minimumNumberOfSeats()
  {
    return 1;
  }

  public Seat addSeat(int aSeatNumber, Table aTable)
  {
    Seat aNewSeat = new Seat(aSeatNumber, aTable, this);
    return aNewSeat;
  }

  public boolean addSeat(Seat aSeat)
  {
    boolean wasAdded = false;
    if (seats.contains(aSeat)) { return false; }
    Bill existingBill = aSeat.getBill();
    boolean isNewBill = existingBill != null && !this.equals(existingBill);

    if (isNewBill && existingBill.numberOfSeats() <= minimumNumberOfSeats())
    {
      return wasAdded;
    }
    if (isNewBill)
    {
      aSeat.setBill(this);
    }
    else
    {
      seats.add(aSeat);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSeat(Seat aSeat)
  {
    boolean wasRemoved = false;
    //Unable to remove aSeat, as it must always have a bill
    if (this.equals(aSeat.getBill()))
    {
      return wasRemoved;
    }

    //bill already at minimum (1)
    if (numberOfSeats() <= minimumNumberOfSeats())
    {
      return wasRemoved;
    }

    seats.remove(aSeat);
    wasRemoved = true;
    return wasRemoved;
  }

  public boolean addSeatAt(Seat aSeat, int index)
  {  
    boolean wasAdded = false;
    if(addSeat(aSeat))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSeats()) { index = numberOfSeats() - 1; }
      seats.remove(aSeat);
      seats.add(index, aSeat);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSeatAt(Seat aSeat, int index)
  {
    boolean wasAdded = false;
    if(seats.contains(aSeat))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSeats()) { index = numberOfSeats() - 1; }
      seats.remove(aSeat);
      seats.add(index, aSeat);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSeatAt(aSeat, index);
    }
    return wasAdded;
  }

  public boolean isNumberOfTablesValid()
  {
    boolean isValid = numberOfTables() >= minimumNumberOfTables();
    return isValid;
  }

  public static int minimumNumberOfTables()
  {
    return 1;
  }

  public boolean addTable(Table aTable)
  {
    boolean wasAdded = false;
    if (tables.contains(aTable)) { return false; }
    tables.add(aTable);
    if (aTable.indexOfBill(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aTable.addBill(this);
      if (!wasAdded)
      {
        tables.remove(aTable);
      }
    }
    return wasAdded;
  }

  public boolean removeTable(Table aTable)
  {
    boolean wasRemoved = false;
    if (!tables.contains(aTable))
    {
      return wasRemoved;
    }

    if (numberOfTables() <= minimumNumberOfTables())
    {
      return wasRemoved;
    }

    int oldIndex = tables.indexOf(aTable);
    tables.remove(oldIndex);
    if (aTable.indexOfBill(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aTable.removeBill(this);
      if (!wasRemoved)
      {
        tables.add(oldIndex,aTable);
      }
    }
    return wasRemoved;
  }

  public boolean setTables(Table... newTables)
  {
    boolean wasSet = false;
    ArrayList<Table> verifiedTables = new ArrayList<Table>();
    for (Table aTable : newTables)
    {
      if (verifiedTables.contains(aTable))
      {
        continue;
      }
      verifiedTables.add(aTable);
    }

    if (verifiedTables.size() != newTables.length || verifiedTables.size() < minimumNumberOfTables())
    {
      return wasSet;
    }

    ArrayList<Table> oldTables = new ArrayList<Table>(tables);
    tables.clear();
    for (Table aNewTable : verifiedTables)
    {
      tables.add(aNewTable);
      if (oldTables.contains(aNewTable))
      {
        oldTables.remove(aNewTable);
      }
      else
      {
        aNewTable.addBill(this);
      }
    }

    for (Table anOldTable : oldTables)
    {
      anOldTable.removeBill(this);
    }
    wasSet = true;
    return wasSet;
  }

  public boolean addTableAt(Table aTable, int index)
  {  
    boolean wasAdded = false;
    if(addTable(aTable))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTables()) { index = numberOfTables() - 1; }
      tables.remove(aTable);
      tables.add(index, aTable);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTableAt(Table aTable, int index)
  {
    boolean wasAdded = false;
    if(tables.contains(aTable))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTables()) { index = numberOfTables() - 1; }
      tables.remove(aTable);
      tables.add(index, aTable);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTableAt(aTable, index);
    }
    return wasAdded;
  }

  public boolean isNumberOfOrdersValid()
  {
    boolean isValid = numberOfOrders() >= minimumNumberOfOrders();
    return isValid;
  }

  public static int minimumNumberOfOrders()
  {
    return 1;
  }

  public boolean addOrder(Order aOrder)
  {
    boolean wasAdded = false;
    if (orders.contains(aOrder)) { return false; }
    orders.add(aOrder);
    if (aOrder.indexOfBill(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aOrder.addBill(this);
      if (!wasAdded)
      {
        orders.remove(aOrder);
      }
    }
    return wasAdded;
  }

  public boolean removeOrder(Order aOrder)
  {
    boolean wasRemoved = false;
    if (!orders.contains(aOrder))
    {
      return wasRemoved;
    }

    if (numberOfOrders() <= minimumNumberOfOrders())
    {
      return wasRemoved;
    }

    int oldIndex = orders.indexOf(aOrder);
    orders.remove(oldIndex);
    if (aOrder.indexOfBill(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aOrder.removeBill(this);
      if (!wasRemoved)
      {
        orders.add(oldIndex,aOrder);
      }
    }
    return wasRemoved;
  }

  public boolean setOrders(Order... newOrders)
  {
    boolean wasSet = false;
    ArrayList<Order> verifiedOrders = new ArrayList<Order>();
    for (Order aOrder : newOrders)
    {
      if (verifiedOrders.contains(aOrder))
      {
        continue;
      }
      verifiedOrders.add(aOrder);
    }

    if (verifiedOrders.size() != newOrders.length || verifiedOrders.size() < minimumNumberOfOrders())
    {
      return wasSet;
    }

    ArrayList<Order> oldOrders = new ArrayList<Order>(orders);
    orders.clear();
    for (Order aNewOrder : verifiedOrders)
    {
      orders.add(aNewOrder);
      if (oldOrders.contains(aNewOrder))
      {
        oldOrders.remove(aNewOrder);
      }
      else
      {
        aNewOrder.addBill(this);
      }
    }

    for (Order anOldOrder : oldOrders)
    {
      anOldOrder.removeBill(this);
    }
    wasSet = true;
    return wasSet;
  }

  public boolean addOrderAt(Order aOrder, int index)
  {  
    boolean wasAdded = false;
    if(addOrder(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderAt(Order aOrder, int index)
  {
    boolean wasAdded = false;
    if(orders.contains(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderAt(aOrder, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=seats.size(); i > 0; i--)
    {
      Seat aSeat = seats.get(i - 1);
      aSeat.delete();
    }
    ArrayList<Table> copyOfTables = new ArrayList<Table>(tables);
    tables.clear();
    for(Table aTable : copyOfTables)
    {
      if (aTable.numberOfBills() <= Table.minimumNumberOfBills())
      {
        aTable.delete();
      }
      else
      {
        aTable.removeBill(this);
      }
    }
    ArrayList<Order> copyOfOrders = new ArrayList<Order>(orders);
    orders.clear();
    for(Order aOrder : copyOfOrders)
    {
      if (aOrder.numberOfBills() <= Order.minimumNumberOfBills())
      {
        aOrder.delete();
      }
      else
      {
        aOrder.removeBill(this);
      }
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "total" + ":" + getTotal()+ "]";
  }
}