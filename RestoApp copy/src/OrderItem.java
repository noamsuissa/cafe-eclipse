/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/


import java.util.*;

// line 61 "iteration1.ump"
public class OrderItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //OrderItem Associations
  private List<Seat> seats;
  private List<Order> orders;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public OrderItem()
  {
    seats = new ArrayList<Seat>();
    orders = new ArrayList<Order>();
  }

  //------------------------
  // INTERFACE
  //------------------------

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

  public static int minimumNumberOfSeats()
  {
    return 0;
  }

  public boolean addSeat(Seat aSeat)
  {
    boolean wasAdded = false;
    if (seats.contains(aSeat)) { return false; }
    seats.add(aSeat);
    if (aSeat.indexOfOrderItem(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aSeat.addOrderItem(this);
      if (!wasAdded)
      {
        seats.remove(aSeat);
      }
    }
    return wasAdded;
  }

  public boolean removeSeat(Seat aSeat)
  {
    boolean wasRemoved = false;
    if (!seats.contains(aSeat))
    {
      return wasRemoved;
    }

    int oldIndex = seats.indexOf(aSeat);
    seats.remove(oldIndex);
    if (aSeat.indexOfOrderItem(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aSeat.removeOrderItem(this);
      if (!wasRemoved)
      {
        seats.add(oldIndex,aSeat);
      }
    }
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
    if (aOrder.indexOfOrderItem(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aOrder.addOrderItem(this);
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
    if (aOrder.indexOfOrderItem(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aOrder.removeOrderItem(this);
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
        aNewOrder.addOrderItem(this);
      }
    }

    for (Order anOldOrder : oldOrders)
    {
      anOldOrder.removeOrderItem(this);
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
    ArrayList<Seat> copyOfSeats = new ArrayList<Seat>(seats);
    seats.clear();
    for(Seat aSeat : copyOfSeats)
    {
      aSeat.removeOrderItem(this);
    }
    ArrayList<Order> copyOfOrders = new ArrayList<Order>(orders);
    orders.clear();
    for(Order aOrder : copyOfOrders)
    {
      if (aOrder.numberOfOrderItems() <= Order.minimumNumberOfOrderItems())
      {
        aOrder.delete();
      }
      else
      {
        aOrder.removeOrderItem(this);
      }
    }
  }

}