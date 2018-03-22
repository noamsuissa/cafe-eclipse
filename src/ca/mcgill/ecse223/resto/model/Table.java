/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse223.resto.model;
import java.io.Serializable;
import java.util.*;

// line 85 "../../../../../RestoAppPersistence.ump"
// line 1 "../../../../../RestoAppTableStateMachine.ump"
// line 28 "../../../../../RestoApp v3.ump"
public class Table implements Serializable
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Table> tablesByNumber = new HashMap<Integer, Table>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Table Attributes
  private int number;
  private int x;
  private int y;
  private int width;
  private int length;

  //Table State Machines
  public enum Status { Available, NothingOrdered, Ordered }
  private Status status;

  //Table Associations
  private List<Seat> seats;
  private List<Seat> currentSeats;
  private RestoApp restoApp;
  private List<Reservation> reservations;
  private List<Order> orders;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Table(int aNumber, int aX, int aY, int aWidth, int aLength, RestoApp aRestoApp)
  {
    x = aX;
    y = aY;
    width = aWidth;
    length = aLength;
    if (!setNumber(aNumber))
    {
      throw new RuntimeException("Cannot create due to duplicate number");
    }
    seats = new ArrayList<Seat>();
    currentSeats = new ArrayList<Seat>();
    boolean didAddRestoApp = setRestoApp(aRestoApp);
    if (!didAddRestoApp)
    {
      throw new RuntimeException("Unable to create table due to restoApp");
    }
    reservations = new ArrayList<Reservation>();
    orders = new ArrayList<Order>();
    setStatus(Status.Available);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNumber(int aNumber)
  {
    boolean wasSet = false;
    Integer anOldNumber = getNumber();
    if (hasWithNumber(aNumber)) {
      return wasSet;
    }
    number = aNumber;
    wasSet = true;
    if (anOldNumber != null) {
      tablesByNumber.remove(anOldNumber);
    }
    tablesByNumber.put(aNumber, this);
    return wasSet;
  }

  public boolean setX(int aX)
  {
    boolean wasSet = false;
    x = aX;
    wasSet = true;
    return wasSet;
  }

  public boolean setY(int aY)
  {
    boolean wasSet = false;
    y = aY;
    wasSet = true;
    return wasSet;
  }

  public boolean setWidth(int aWidth)
  {
    boolean wasSet = false;
    width = aWidth;
    wasSet = true;
    return wasSet;
  }

  public boolean setLength(int aLength)
  {
    boolean wasSet = false;
    length = aLength;
    wasSet = true;
    return wasSet;
  }

  public int getNumber()
  {
    return number;
  }

  public static Table getWithNumber(int aNumber)
  {
    return tablesByNumber.get(aNumber);
  }

  public static boolean hasWithNumber(int aNumber)
  {
    return getWithNumber(aNumber) != null;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }

  public int getWidth()
  {
    return width;
  }

  public int getLength()
  {
    return length;
  }

  public String getStatusFullName()
  {
    String answer = status.toString();
    return answer;
  }

  public Status getStatus()
  {
    return status;
  }

  public boolean startOrder()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Available:
        // line 4 "../../../../../RestoAppTableStateMachine.ump"
        new Order(new java.sql.Date(Calendar.getInstance().getTime().getTime()), new java.sql.Time(Calendar.getInstance().getTime().getTime()), this.getRestoApp(), this);
        setStatus(Status.NothingOrdered);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean addToOrder(Order o)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Available:
        // line 7 "../../../../../RestoAppTableStateMachine.ump"
        o.addTable(this);
        setStatus(Status.NothingOrdered);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean orderItem(int quantity,Order o,Seat s,PricedMenuItem i)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case NothingOrdered:
        if (quantityNotNegative(quantity))
        {
        // line 12 "../../../../../RestoAppTableStateMachine.ump"
          // create a new order item with the provided quantity, order, seat, and priced menu item
          setStatus(Status.Ordered);
          wasEventProcessed = true;
          break;
        }
        break;
      case Ordered:
        if (quantityNotNegative(quantity))
        {
        // line 32 "../../../../../RestoAppTableStateMachine.ump"
          // create a new order item with the provided quantity, order, seat, and priced menu item
          setStatus(Status.Ordered);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean addToOrderItem(OrderItem i,Seat s)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case NothingOrdered:
        // line 17 "../../../../../RestoAppTableStateMachine.ump"
        List<Seat> seats = i.getSeats();
			if(!seats.contains(s)) {
				i.addSeat(s);		
				}
        setStatus(Status.Ordered);
        wasEventProcessed = true;
        break;
      case Ordered:
        // line 35 "../../../../../RestoAppTableStateMachine.ump"
        // add provided seat to provided order item unless seat has already been added, in which case nothing needs to be done
        setStatus(Status.Ordered);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean endOrder(Order o)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case NothingOrdered:
        // line 23 "../../../../../RestoAppTableStateMachine.ump"
        if (!o.removeTable(this)) {
               if (o.numberOfTables() == 1) {
                  o.delete();
               }
            }
        setStatus(Status.Available);
        wasEventProcessed = true;
        break;
      case Ordered:
        if (allSeatsBilled())
        {
        // line 57 "../../../../../RestoAppTableStateMachine.ump"
          
          setStatus(Status.Available);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancelOrderItem(OrderItem i)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Ordered:
        if (iIsLastItem(i))
        {
        // line 38 "../../../../../RestoAppTableStateMachine.ump"
          // delete order item
          setStatus(Status.NothingOrdered);
          wasEventProcessed = true;
          break;
        }
        if (!(iIsLastItem(i)))
        {
        // line 41 "../../../../../RestoAppTableStateMachine.ump"
          // delete order item
          setStatus(Status.Ordered);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancelOrder()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Ordered:
        // line 44 "../../../../../RestoAppTableStateMachine.ump"
        // delete all order items of the table
        setStatus(Status.NothingOrdered);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean billForSeat(Order o,Seat s)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Ordered:
        // line 47 "../../../../../RestoAppTableStateMachine.ump"
        // create a new bill with the provided order and seat; if the provided seat is already assigned to
            // another bill for the current order, then the seat is first removed from the other bill and if no seats
            // are left for the bill, the bill is deleted
        setStatus(Status.Ordered);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean addToBill(Bill b,Seat s)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Ordered:
        // line 52 "../../../../../RestoAppTableStateMachine.ump"
        // add provided seat to provided bill unless seat has already been added, in which case nothing needs
            // to be done; if the provided seat is already assigned to another bill for the current order, then the
            // seat is first removed from the other bill and if no seats are left for the bill, the bill is deleted
        setStatus(Status.Ordered);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setStatus(Status aStatus)
  {
    status = aStatus;
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

  public Seat getCurrentSeat(int index)
  {
    Seat aCurrentSeat = currentSeats.get(index);
    return aCurrentSeat;
  }

  /**
   * subsets seats
   */
  public List<Seat> getCurrentSeats()
  {
    List<Seat> newCurrentSeats = Collections.unmodifiableList(currentSeats);
    return newCurrentSeats;
  }

  public int numberOfCurrentSeats()
  {
    int number = currentSeats.size();
    return number;
  }

  public boolean hasCurrentSeats()
  {
    boolean has = currentSeats.size() > 0;
    return has;
  }

  public int indexOfCurrentSeat(Seat aCurrentSeat)
  {
    int index = currentSeats.indexOf(aCurrentSeat);
    return index;
  }

  public RestoApp getRestoApp()
  {
    return restoApp;
  }

  public Reservation getReservation(int index)
  {
    Reservation aReservation = reservations.get(index);
    return aReservation;
  }

  public List<Reservation> getReservations()
  {
    List<Reservation> newReservations = Collections.unmodifiableList(reservations);
    return newReservations;
  }

  public int numberOfReservations()
  {
    int number = reservations.size();
    return number;
  }

  public boolean hasReservations()
  {
    boolean has = reservations.size() > 0;
    return has;
  }

  public int indexOfReservation(Reservation aReservation)
  {
    int index = reservations.indexOf(aReservation);
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

  public Seat addSeat()
  {
    Seat aNewSeat = new Seat(this);
    return aNewSeat;
  }

  public boolean addSeat(Seat aSeat)
  {
    boolean wasAdded = false;
    if (seats.contains(aSeat)) { return false; }
    Table existingTable = aSeat.getTable();
    boolean isNewTable = existingTable != null && !this.equals(existingTable);

    if (isNewTable && existingTable.numberOfSeats() <= minimumNumberOfSeats())
    {
      return wasAdded;
    }
    if (isNewTable)
    {
      aSeat.setTable(this);
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
    //Unable to remove aSeat, as it must always have a table
    if (this.equals(aSeat.getTable()))
    {
      return wasRemoved;
    }

    //table already at minimum (1)
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

  public static int minimumNumberOfCurrentSeats()
  {
    return 0;
  }

  public boolean addCurrentSeat(Seat aCurrentSeat)
  {
    boolean wasAdded = false;
    if (currentSeats.contains(aCurrentSeat)) { return false; }
    currentSeats.add(aCurrentSeat);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCurrentSeat(Seat aCurrentSeat)
  {
    boolean wasRemoved = false;
    if (currentSeats.contains(aCurrentSeat))
    {
      currentSeats.remove(aCurrentSeat);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addCurrentSeatAt(Seat aCurrentSeat, int index)
  {  
    boolean wasAdded = false;
    if(addCurrentSeat(aCurrentSeat))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCurrentSeats()) { index = numberOfCurrentSeats() - 1; }
      currentSeats.remove(aCurrentSeat);
      currentSeats.add(index, aCurrentSeat);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCurrentSeatAt(Seat aCurrentSeat, int index)
  {
    boolean wasAdded = false;
    if(currentSeats.contains(aCurrentSeat))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCurrentSeats()) { index = numberOfCurrentSeats() - 1; }
      currentSeats.remove(aCurrentSeat);
      currentSeats.add(index, aCurrentSeat);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCurrentSeatAt(aCurrentSeat, index);
    }
    return wasAdded;
  }

  public boolean setRestoApp(RestoApp aRestoApp)
  {
    boolean wasSet = false;
    if (aRestoApp == null)
    {
      return wasSet;
    }

    RestoApp existingRestoApp = restoApp;
    restoApp = aRestoApp;
    if (existingRestoApp != null && !existingRestoApp.equals(aRestoApp))
    {
      existingRestoApp.removeTable(this);
    }
    restoApp.addTable(this);
    wasSet = true;
    return wasSet;
  }

  public static int minimumNumberOfReservations()
  {
    return 0;
  }

  public boolean addReservation(Reservation aReservation)
  {
    boolean wasAdded = false;
    if (reservations.contains(aReservation)) { return false; }
    reservations.add(aReservation);
    if (aReservation.indexOfTable(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aReservation.addTable(this);
      if (!wasAdded)
      {
        reservations.remove(aReservation);
      }
    }
    return wasAdded;
  }

  public boolean removeReservation(Reservation aReservation)
  {
    boolean wasRemoved = false;
    if (!reservations.contains(aReservation))
    {
      return wasRemoved;
    }

    int oldIndex = reservations.indexOf(aReservation);
    reservations.remove(oldIndex);
    if (aReservation.indexOfTable(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aReservation.removeTable(this);
      if (!wasRemoved)
      {
        reservations.add(oldIndex,aReservation);
      }
    }
    return wasRemoved;
  }

  public boolean addReservationAt(Reservation aReservation, int index)
  {  
    boolean wasAdded = false;
    if(addReservation(aReservation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReservations()) { index = numberOfReservations() - 1; }
      reservations.remove(aReservation);
      reservations.add(index, aReservation);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveReservationAt(Reservation aReservation, int index)
  {
    boolean wasAdded = false;
    if(reservations.contains(aReservation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReservations()) { index = numberOfReservations() - 1; }
      reservations.remove(aReservation);
      reservations.add(index, aReservation);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addReservationAt(aReservation, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfOrders()
  {
    return 0;
  }

  public boolean addOrder(Order aOrder)
  {
    boolean wasAdded = false;
    if (orders.contains(aOrder)) { return false; }
    orders.add(aOrder);
    if (aOrder.indexOfTable(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aOrder.addTable(this);
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

    int oldIndex = orders.indexOf(aOrder);
    orders.remove(oldIndex);
    if (aOrder.indexOfTable(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aOrder.removeTable(this);
      if (!wasRemoved)
      {
        orders.add(oldIndex,aOrder);
      }
    }
    return wasRemoved;
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
    tablesByNumber.remove(getNumber());
    while (seats.size() > 0)
    {
      Seat aSeat = seats.get(seats.size() - 1);
      aSeat.delete();
      seats.remove(aSeat);
    }
    
    currentSeats.clear();
    RestoApp placeholderRestoApp = restoApp;
    this.restoApp = null;
    if(placeholderRestoApp != null)
    {
      placeholderRestoApp.removeTable(this);
    }
    ArrayList<Reservation> copyOfReservations = new ArrayList<Reservation>(reservations);
    reservations.clear();
    for(Reservation aReservation : copyOfReservations)
    {
      if (aReservation.numberOfTables() <= Reservation.minimumNumberOfTables())
      {
        aReservation.delete();
      }
      else
      {
        aReservation.removeTable(this);
      }
    }
    ArrayList<Order> copyOfOrders = new ArrayList<Order>(orders);
    orders.clear();
    for(Order aOrder : copyOfOrders)
    {
      if (aOrder.numberOfTables() <= Order.minimumNumberOfTables())
      {
        aOrder.delete();
      }
      else
      {
        aOrder.removeTable(this);
      }
    }
  }

  // line 90 "../../../../../RestoAppPersistence.ump"
   public static  void reinitializeUniqueNumber(List<Table> tables){
    tablesByNumber = new HashMap<Integer, Table>();
  	for(Table table : tables){
  		tablesByNumber.put(table.getNumber(), table);
  	}
  }


  /**
   * check that the provided quantity is an integer greater than 0
   */
  // line 64 "../../../../../RestoAppTableStateMachine.ump"
   private boolean quantityNotNegative(int quantity){
    if (quantity > 0 ){
      return true;
      }
      return false;
  }


  /**
   * check that the provided order item is the last item of the current order of the table
   */
  // line 72 "../../../../../RestoAppTableStateMachine.ump"
   private boolean iIsLastItem(OrderItem i){
    // TODO
      
      Order order = i.getOrder();
      List <OrderItem> orderItems= order.getOrderItems();
      int size= orderItems.size();
      if(size==1 && orderItems.get(0)==i){
      return true;
      }
      
   return false;
  }

  // line 86 "../../../../../RestoAppTableStateMachine.ump"
   private boolean allSeatsBilled(){
    List<Seat> curSeats = getCurrentSeats(); // get current seats in the whole restaurant
		List<Seat> activeSeats = new ArrayList<Seat>(); // this will be used later to store all active seats at table
		// have to move current seats into list that is modifiable
		List<Seat> tempSeats = new ArrayList< Seat>();
		for( Seat seat: curSeats){
		tempSeats.add(seat);
		}
		
	
		//this section is used to get the last order in the current orders for the table
		List<Order> orders = getOrders();
		int listSize = orders.size();
		Order lastOrder;
		if (listSize == 0) {
			return false;
		} else {
			listSize = listSize - 1;
			lastOrder = orders.get(listSize);
		}
		
		List<Bill> curBills = lastOrder.getBills();// get bills for this order in this table. These are the bills that the seats must have
		List<OrderItem> curOrderItems = lastOrder.getOrderItems();// get order items for this order at this table

		// get all seats that have order item
		for (OrderItem item : curOrderItems) {

			List<Seat> tempList = item.getSeats();
			for (Seat seat : tempList) {
				activeSeats.add(seat);
			}
		}
		// remove seats from all current seats that do not have an order item

		for (Seat seat : tempSeats) {
			if (!activeSeats.contains(seat)) {
				tempSeats.remove(seat);
			}
		}
		// all seats in cur seats are current and have order items 
		// check logic
		int i = 0;
		for (Seat seat : tempSeats) {
			List<Bill> billsOfSeat = seat.getBills();
			for (Bill bill : curBills) {
				if (billsOfSeat.contains(bill)) {
					i += 1;
				}
			}
			if (i == 0) {
				return false;
			}
			i = 0;
		}
		return true;
  }


  /**
   * line 37 "../../../../../RestoApp v3.ump"
   */
  // line 144 "../../../../../RestoAppTableStateMachine.ump"
   public boolean doesOverlap(int x2, int y2, int width2, int length2){
    return x2 < this.x + this.width && x2 + width2 > this.x && y2 < this.y + this.length && y2 + length2 > this.y;
  }


  public String toString()
  {
    return super.toString() + "["+
            "number" + ":" + getNumber()+ "," +
            "x" + ":" + getX()+ "," +
            "y" + ":" + getY()+ "," +
            "width" + ":" + getWidth()+ "," +
            "length" + ":" + getLength()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "restoApp = "+(getRestoApp()!=null?Integer.toHexString(System.identityHashCode(getRestoApp())):"null");
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 88 "../../../../../RestoAppPersistence.ump"
  private static final long serialVersionUID = 8896099581655989380L ;

  
}