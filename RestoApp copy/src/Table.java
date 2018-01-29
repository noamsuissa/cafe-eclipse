/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/


import java.util.*;

// line 51 "iteration1.ump"
public class Table
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Table Attributes
  private int tableNumber;
  private boolean tableReserved;

  //Table Associations
  private List<Reservation> reservations;
  private List<Bill> bills;
  private List<Seat> seats;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Table(int aTableNumber, boolean aTableReserved)
  {
    tableNumber = aTableNumber;
    tableReserved = aTableReserved;
    reservations = new ArrayList<Reservation>();
    bills = new ArrayList<Bill>();
    seats = new ArrayList<Seat>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTableNumber(int aTableNumber)
  {
    boolean wasSet = false;
    tableNumber = aTableNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setTableReserved(boolean aTableReserved)
  {
    boolean wasSet = false;
    tableReserved = aTableReserved;
    wasSet = true;
    return wasSet;
  }

  public int getTableNumber()
  {
    return tableNumber;
  }

  public boolean getTableReserved()
  {
    return tableReserved;
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

  public boolean isNumberOfBillsValid()
  {
    boolean isValid = numberOfBills() >= minimumNumberOfBills();
    return isValid;
  }

  public static int minimumNumberOfBills()
  {
    return 1;
  }

  public boolean addBill(Bill aBill)
  {
    boolean wasAdded = false;
    if (bills.contains(aBill)) { return false; }
    bills.add(aBill);
    if (aBill.indexOfTable(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aBill.addTable(this);
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

    if (numberOfBills() <= minimumNumberOfBills())
    {
      return wasRemoved;
    }

    int oldIndex = bills.indexOf(aBill);
    bills.remove(oldIndex);
    if (aBill.indexOfTable(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aBill.removeTable(this);
      if (!wasRemoved)
      {
        bills.add(oldIndex,aBill);
      }
    }
    return wasRemoved;
  }

  public boolean setBills(Bill... newBills)
  {
    boolean wasSet = false;
    ArrayList<Bill> verifiedBills = new ArrayList<Bill>();
    for (Bill aBill : newBills)
    {
      if (verifiedBills.contains(aBill))
      {
        continue;
      }
      verifiedBills.add(aBill);
    }

    if (verifiedBills.size() != newBills.length || verifiedBills.size() < minimumNumberOfBills())
    {
      return wasSet;
    }

    ArrayList<Bill> oldBills = new ArrayList<Bill>(bills);
    bills.clear();
    for (Bill aNewBill : verifiedBills)
    {
      bills.add(aNewBill);
      if (oldBills.contains(aNewBill))
      {
        oldBills.remove(aNewBill);
      }
      else
      {
        aNewBill.addTable(this);
      }
    }

    for (Bill anOldBill : oldBills)
    {
      anOldBill.removeTable(this);
    }
    wasSet = true;
    return wasSet;
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

  public boolean isNumberOfSeatsValid()
  {
    boolean isValid = numberOfSeats() >= minimumNumberOfSeats();
    return isValid;
  }

  public static int minimumNumberOfSeats()
  {
    return 1;
  }

  public Seat addSeat(int aSeatNumber, Bill aBill)
  {
    Seat aNewSeat = new Seat(aSeatNumber, this, aBill);
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

  public void delete()
  {
    ArrayList<Reservation> copyOfReservations = new ArrayList<Reservation>(reservations);
    reservations.clear();
    for(Reservation aReservation : copyOfReservations)
    {
      aReservation.removeTable(this);
    }
    ArrayList<Bill> copyOfBills = new ArrayList<Bill>(bills);
    bills.clear();
    for(Bill aBill : copyOfBills)
    {
      if (aBill.numberOfTables() <= Bill.minimumNumberOfTables())
      {
        aBill.delete();
      }
      else
      {
        aBill.removeTable(this);
      }
    }
    for(int i=seats.size(); i > 0; i--)
    {
      Seat aSeat = seats.get(i - 1);
      aSeat.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "tableNumber" + ":" + getTableNumber()+ "," +
            "tableReserved" + ":" + getTableReserved()+ "]";
  }
}