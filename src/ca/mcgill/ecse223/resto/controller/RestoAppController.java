package ca.mcgill.ecse223.resto.controller;


import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.mcgill.ecse223.resto.application.*;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.model.*;
import ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory;

public class RestoAppController {
	public RestoAppController() {
	}

	public static void addMenuItem(String name, ItemCategory category, double price) throws InvalidInputException {
		String error="";
		if(name == null) {
			error += "Name cannot be null. ";
		}if(category == null) {
			error += "Category cannot be null. ";
		}if(price <= 0) {
			error += "price cannot be zero or negative. ";
		}if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		} 

		RestoApp r = RestoAppApplication.getRestoApp();
		Menu menu = r.getMenu();
		try {
			MenuItem menuItem = new MenuItem(name, price, menu);
			menuItem.setItemCategory(category);
			PricedMenuItem pmi = menuItem.addPricedMenuItem(price, r);
			menuItem.setCurrentPricedMenuItem(pmi);
		}catch(RuntimeException e) {
			throw new InvalidInputException("Name is a duplicate");
		}
		
		try {
			RestoAppApplication.save();
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

	}
	
	public static void orderMenuItem (MenuItem aMenuItem, int quantity,Seat... allSeats) throws InvalidInputException {
		RestoApp r = RestoAppApplication.getRestoApp();
		boolean current = aMenuItem.hasCurrentPricedMenuItem();
		if (current==false) {
			
			throw new InvalidInputException("order item is not current ");
		} 
		List<Table> currentTables = r.getCurrentTables();
		Order lastOrder=null;
		Order comparedOrder=null;// if declare compare order in if else, cannot checl of compared order equals last order
		for(Seat seat: allSeats) {
			Table table= seat.getTable();
			if (!currentTables.contains(table)) {
				throw new InvalidInputException("table is not current");
			}
			List<Seat> curSeats= table.getCurrentSeats();
			if (!curSeats.contains(seat)) {
				throw new InvalidInputException("seat is not current");
			}
		
			if(lastOrder==null) {
				if(table.numberOfOrders()>0) {
				 lastOrder= table.getOrder(table.numberOfOrders()-1);
					}
				else {
					throw new InvalidInputException("table has no order");
				}
				
			}
			else {
				if (table.numberOfOrders()>0) {
					comparedOrder= table.getOrder(table.numberOfOrders()-1);
				}
				else {
					throw new InvalidInputException("table has no order");
				}
			}
		if(!comparedOrder.equals(lastOrder)) {
			throw new InvalidInputException("error with last order");
			}
		
	if(lastOrder==null) {
		throw new InvalidInputException("Order was not found");
		}
	}
	PricedMenuItem pmi= aMenuItem.getCurrentPricedMenuItem();
	OrderItem newItem= null;
	boolean itemCreated= false;
	OrderItem lastItem=null; // last item must be created outside the if else statements
		for(Seat seat: allSeats) {
			Table table= seat.getTable();
		
			if(itemCreated==true) {
				table.addToOrderItem(newItem,seat);
			}
			else {
				
				if(lastOrder.numberOfOrderItems()>0) {
					lastItem=lastOrder.getOrderItem(lastOrder.numberOfOrderItems()-1);
				}
				table.orderItem(quantity,lastOrder,seat, pmi);
			}
			if(lastOrder.numberOfOrderItems()>0 && !lastOrder.getOrderItem(lastOrder.numberOfOrderItems()-1).equals(lastItem)) {
			itemCreated=true;
			newItem=lastOrder.getOrderItem(lastOrder.numberOfOrderItems()-1);
			}
		}
	if(itemCreated== false) {
		throw new InvalidInputException("Item was not created");
	}
	try {
		RestoAppApplication.save();
	}
	catch (RuntimeException e) {
		throw new InvalidInputException(e.getMessage());
	}	
}
	
	public static void removeMenuItem(MenuItem menuItem) throws InvalidInputException{
		String error="";
		if(menuItem == null) {
			error += "You must choose a menu item. ";
		}if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
		
		boolean current = menuItem.hasCurrentPricedMenuItem();
		
		if(!current) {
			throw new InvalidInputException("There is no current item. ");
		}
		
		menuItem.setCurrentPricedMenuItem(null);
		
		try {
			RestoAppApplication.save();
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}
	
	public static void updateMenuItem(MenuItem menuItem, String name, ItemCategory category, double price) throws InvalidInputException{
		String error="";
		if(name == null) {
			error += "Name cannot be null. ";
		}if(category == null) {
			error += "Category cannot be null. ";
		}if(price <= 0) {
			error += "price cannot be zero or negative. ";
		}if(menuItem == null) {
			error += "menuItem cannot be null";
		}if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		} 
		
		if(!menuItem.hasCurrentPricedMenuItem()) {
			throw new InvalidInputException("There is no current item. ");
		}
		
		//no checks because it is okay to keep same name/category and only want to change price
		menuItem.setName(name);
		menuItem.setItemCategory(category);
		
		if(price != menuItem.getCurrentPricedMenuItem().getPrice()) {
			RestoApp r = RestoAppApplication.getRestoApp();
			PricedMenuItem pmi = menuItem.addPricedMenuItem(price, r);
			menuItem.setCurrentPricedMenuItem(pmi);
		}
		
		try {
			RestoAppApplication.save();
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	public static void startOrder (List<Table> tables) throws InvalidInputException {

		boolean orderCreated = false;
		Order newOrder = null;

		if (tables == null) {

			throw new InvalidInputException("Error. Table is null");
		}

		RestoApp r = RestoAppApplication.getRestoApp();

		List<Table> currentTables = r.getCurrentTables();

		for (Table table : tables) {
			boolean current = currentTables.contains(table);
			if(!current) {
				throw new InvalidInputException("Cannot start an order on this table");
			}
		}

		for (Table table : tables) { 
			if(orderCreated){ 
				table.addToOrder(newOrder); 
			} 

			else{
				Order lastOrder = null;
				if(table.numberOfOrders()>0){
					lastOrder = table.getOrder(table.numberOfOrders()-1);
				}

				table.startOrder();

				if((table.numberOfOrders()>0) && !(table.getOrder(table.numberOfOrders()-1)).equals(lastOrder)) {
					orderCreated = true;
					newOrder = table.getOrder(table.numberOfOrders()-1);

				}

			}

		}

		if(!orderCreated){ 
			throw new InvalidInputException("All tables are in use");
		}

		r.addCurrentOrder(newOrder);

		try {
			RestoAppApplication.save();
			System.out.println("save worked start order");
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

	}

	public static void endOrder (Order order) throws InvalidInputException {

		if (order == null) {

			throw new InvalidInputException("Error. Order is null.");
		}

		RestoApp r = RestoAppApplication.getRestoApp();

		List<Order> currentOrders = r.getCurrentOrders();

		boolean current = currentOrders.contains(order);

		if(!current) {
			throw new InvalidInputException("Order is not in current orders");
		}

		List<Table> tables = order.getTables();

		for (Table table : tables) { 
			if(table.numberOfOrders()>0 && table.getOrder(table.numberOfOrders()-1).equals(order)) {

				table.endOrder(order);

			}
		}

		if(allTablesAvailableOrDifferentCurrentOrder(tables,order)){
			r.removeCurrentOrder(order);
		}

		try {
			RestoAppApplication.save();
			System.out.println("save worked end order");
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

	}

	private static boolean allTablesAvailableOrDifferentCurrentOrder(List<Table> tables, Order order) {

		for(Table table: tables)
		{
			if(table.getStatusFullName()!="Available")
			{
				int x = table.numberOfOrders()-1;
				Order lastOrder = table.getOrder(x);
				if (order.equals(lastOrder))
				{
					return false; 
				}
			}
		}
		return true;
	}

	public static void moveTable(Table table, int x, int y) throws InvalidInputException{
		String error="";
		int width, length;
		if(table == null) {
			error += "Table cannot be null. ";
		}if(x<0) {
			error += "x cannot be negative. ";
		}if(y<0) {
			error += "y cannot be negative. ";
		}if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}

		width = table.getWidth();
		length = table.getLength();
		RestoApp restoApp = RestoAppApplication.getRestoApp();
		List<Table> curTables = restoApp.getCurrentTables();
		for (Table curTable : curTables) {
			if(curTable.doesOverlap(x,y,width,length) && curTable != table) {
				error = "There is an overlap. ";
				if(error.length()>0) {
					throw new InvalidInputException(error.trim());
				}
			}
		}
		table.setX(x);
		table.setY(y);

		try {
			RestoAppApplication.save();
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}


	public static void removeTable (Table aTable) throws InvalidInputException {
		String error = "";
		if (aTable == null) {
			error = "Must enter a table to remove. ";
		}
		boolean reserved = aTable.hasReservations();
		if(reserved) {
			error = error + "Cannot remove table because it is reserved. ";
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}

		RestoApp r = RestoAppApplication.getRestoApp();

		List<Order> currentOrders = r.getCurrentOrders();

		for(Order order : currentOrders) {
			List<Table> tables = order.getTables();
			boolean inUse = tables.contains(aTable);
			if(inUse) {
				throw new InvalidInputException("Cannot remove table because it is in use.");
			}
		}

		r.removeCurrentTable(aTable);
		try {
			RestoAppApplication.save();
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	public static void addTable(int aNumber, int aX, int aY, int aWidth, int aLength, int seats) throws InvalidInputException{
		String error = "";
		if (aX < 0) {
			error="The x coordinate cannot be negative ";
		}
		if (aY <0) {
			error= error +"The y coordinate cannot be negative ";
		}
		if ( aWidth <= 0) {
			error= error +"The width must be positive ";
		}
		if (aLength<=0) {
			error= error + "The length must be positive ";
		}
		if (seats <= 0) {
			error = error +"the number of seats must be positive ";
		}
		if (aNumber<=0) {
			error= error + "The table number must be positive ";
		}
		if(error.length()>0){
			throw new InvalidInputException (error.trim());
		}
		RestoApp r = RestoAppApplication.getRestoApp();
		List<Table> currentTables = r.getCurrentTables();
		for (Table currentTable : currentTables )
		{

			if (currentTable.doesOverlap(aX,aY, aWidth, aLength)) {

				throw new InvalidInputException("There is already a table in this location");
			}
		}
		try{
			Table table = new Table(aNumber,aX, aY, aWidth, aLength, r);
			r.addCurrentTable(table);
			for (int i=1;i<= seats ;i++ ) {
				Seat seat= table.addSeat();
				table.addCurrentSeat(seat);
			}
			RestoAppApplication.save();
		}
		catch(RuntimeException e){
			throw new InvalidInputException(e.getMessage());
		}

	}
	public static List<ItemCategory> getItemCategory(){
		return Arrays.asList(ItemCategory.values());
	}

	public static List<MenuItem> getMenuItems(ItemCategory itemCategory) throws InvalidInputException {
		ArrayList<MenuItem> CategoryItems = new ArrayList<MenuItem>();
		RestoApp restoApp = RestoAppApplication.getRestoApp();
		Menu menu = restoApp.getMenu();
		try {
			for (MenuItem menuItem : menu.getMenuItems()) {
				boolean current = menuItem.hasCurrentPricedMenuItem();
				ItemCategory category = menuItem.getItemCategory();

				if(current && category.equals(itemCategory)) {
					CategoryItems.add(menuItem);
				}
			}
			return CategoryItems;
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	public static void updateTable (Table aTable, int aNumber, int seats) throws InvalidInputException {
		String error = "";
		if (aTable == null) {
			error = "Must enter a table to update. ";
		}
		if (aNumber < 0){
			error = error + "The table number must be positive. ";
		}
		if (seats < 0){
			error = error + "The number of seats must be positive. ";
		}
		boolean reserved = aTable.hasReservations();
		if(reserved) {
			error = error + "Cannot remove table because it is reserved. ";
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}

		RestoApp r = RestoAppApplication.getRestoApp();

		List<Order> currentOrders = r.getCurrentOrders();

		for(Order order : currentOrders) {

			List<Table> tables = order.getTables();

			boolean inUse = tables.contains(aTable);
			if(inUse) {
				throw new InvalidInputException("Cannot update table because it is in use.");
			}

		}

		try{
			aTable.setNumber(aNumber);
		}
		catch(RuntimeException e){
			throw new InvalidInputException("Table number cannot be a duplicate");
		}

		int n = aTable.numberOfCurrentSeats();

		for (int i=1;i <= seats-n; i++ ) {
			Seat seat = aTable.addSeat();
			aTable.addCurrentSeat(seat);
		}

		for (int i=1;i <= n-seats; i++ ) {
			Seat seat = aTable.getCurrentSeat(0);
			aTable.removeCurrentSeat(seat);
		}

		try{
			RestoAppApplication.save();
		}
		catch(RuntimeException e){
			throw new InvalidInputException(e.getMessage());
		}



	}

	public static void reserveTable(Date date, Time time, int numberInParty, String contactName, String contactEmailAddress, String contactPhoneNumber, List<Table> tables) throws InvalidInputException{

		String error = "";
		@SuppressWarnings("unused")
		int seatCapacity=0;
		java.util.Date currentDate;
		currentDate=java.util.Calendar.getInstance().getTime(); 


		if(date == null){
			error = "Cannot reserve if there is no reservation date. ";
		}

		if(time == null){
			error = "Cannot reserve if there is no reservation time. ";
		}

		if(contactName == null){
			error = "Cannot reserve if there is no reservation contact name.  ";
		}

		if(contactEmailAddress == null){
			error = "Cannot reserve if there is no reservation contact email. ";
		}

		if(contactPhoneNumber == null){
			error = "Cannot reserve if there is no reservation contact phone number.  ";
		}

		if ((time.getTime() + date.getTime() ) < currentDate.getTime() && (date.compareTo(currentDate) < 0)){
			error = "Cannot reserve for a date in the past. ";
		}

		if(numberInParty < 0){
			error = "Cannot reserve if number in party is a negative number. ";
		}

		if(contactName.length() == 0){
			error = "Must enter a name to reserve. ";
		}

		if(contactEmailAddress.length() == 0){
			error = "Must enter an email address to reserve. ";
		}

		if(contactPhoneNumber.length() == 0){
			error = "Must enter a phone number to reserve. ";
		}


		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}

		RestoApp r = RestoAppApplication.getRestoApp();

		List<Table> currentTables = r.getCurrentTables();

		for (Table table : tables ){

			boolean current = currentTables.contains(table);

			if(!current){
				throw new InvalidInputException("Cannot reserve table because it is not one of the current tables.");
			}

			seatCapacity += table.numberOfCurrentSeats() ;

			List<Reservation> reservations = table.getReservations();

			for(Reservation reservation : reservations){

				if(reservation.doesOverlap(date, time)){

					throw new InvalidInputException("Cannot reseerve because there is an overlap.");
				}

			}

		}

		Table[] tableArr = new Table[tables.size()];
		Reservation res = new Reservation(date, time, numberInParty, contactName, contactEmailAddress, contactPhoneNumber, r, tables.toArray(tableArr));

		try {
			RestoAppApplication.save();
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}


	}
}
