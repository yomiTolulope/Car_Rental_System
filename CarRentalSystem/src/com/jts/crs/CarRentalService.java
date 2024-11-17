package com.jts.crs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CarRentalService {

	private List<Car> cars;
	private List<Customer> customers;
	private List<BookedCarInformation> bookedCarInformations;
	
	public CarRentalService() {
		cars = new ArrayList<>();
		customers = new ArrayList<>();
		bookedCarInformations = new ArrayList<>();
	}
	
	public void bookedCar(Car car, Customer customer, int days) {
		if (car.getNoOfAvailableCar() > 0) {
			car.setNoOfAvailableCar(car.getNoOfAvailableCar() - 1);
			bookedCarInformations.add(new BookedCarInformation(car, customer, days));
		} else {
			System.out.println("Car is not available for rent.");
		}
	}
	
	public void returnCar(Car car, BookedCarInformation bookedCarInformation) {
		car.setNoOfAvailableCar(car.getNoOfAvailableCar() + 1);
		bookedCarInformations.remove(bookedCarInformation);
	}
	public void addCars(Car car) {
		cars.add(car);
	}
	
	public void addCustomer(Customer customer) {
		customers.add(customer);
	}
	
	public void options() {
		Scanner scanner = new Scanner(System.in);
			while (true) {
				System.out.println("===== Welcome to our Car Rental System =====");
				System.out.println("1. Booked a Car");
				System.out.println("2. Return a Booked Car");
				System.out.println("3. Available Car");
				System.out.println("4  Exit");
				
				System.out.println("Enter your choice: ");
				
				int choice = scanner.nextInt();
				scanner.nextLine();
				
				if (choice == 1) {
					System.out.println("== For Renting a Car please provide below detail ==");
					System.out.println("Enter your name: ");
					
					String customerName = scanner.nextLine();
					
					System.out.println("Enter the Car ID you want to rent: ");
					String carId = scanner.nextLine();
					
					System.out.println("Enter the number of days for Rental: ");
					int days = scanner.nextInt();
					
					Customer customer = new Customer("CUSTOMER-" + (customers.size() + 1), customerName);
					addCustomer(customer);
					
					Optional<Car> optionalCar = cars.stream()
					.filter(c -> c.getCarId().equalsIgnoreCase(carId) && c.getNoOfAvailableCar() > 0)
					.findAny();
					
					if (optionalCar.isEmpty()) {
						System.out.println("Car is mot available. Please try to book another car.");
						options();
						return;
					}
					
					Car selectedCar = optionalCar.get();
					
					System.out.println("=== Bill Receipt ===");
					System.out.println("Customer ID: " + customer.getId());
					System.out.println("Customer Name: " + customer.getName());
					System.out.println("Car Brand: " + selectedCar.getBrand() + "Model: " + selectedCar.getModel());
					System.out.println("Rental Days: " + days);
					System.out.println("Total Price: " + selectedCar.calculatePrice(days));
					
					System.out.println("Confirm rental (YES/NO): ");
					String confirmation = scanner.next();
					
					if (confirmation.equalsIgnoreCase("Yes")) {
						// Booked a car
						bookedCar(selectedCar, customer, days);
						System.out.println("Car booking is done successfully.");
					} else {
						System.out.println("Car booking is canceled.");
					}
				} else if (choice == 2) {
					System.out.println("== Return a Car ==");
					System.out.println("Enter the car ID you want to return: ");
					String carId = scanner.nextLine();
							
					Optional<Car> optionalCar =cars.stream()
					.filter(c -> c.getCarId().equals(carId))
					.findAny();
					
					if (optionalCar.isEmpty()) {
						System.out.println("Please provide valid car details.");
						options();
						return;
					}
					
					Car carToReturn = optionalCar.get();
					
					BookedCarInformation bookedCarInformation = bookedCarInformations.stream()
					.filter(b -> b.getCar() == carToReturn).findFirst()
					.orElse(null);
					
					if(bookedCarInformation == null) {
						System.out.println("Car information not available. Please provide valid details.");
						options();
						return;
					}
					
					Customer customer = bookedCarInformation.getCustomer();
					
					returnCar(carToReturn, bookedCarInformation);
					System.out.println("Car returned successfully by " + customer.getName());
				} else if (choice == 3) {
					System.out.println("== Available Cars ==");
					
					cars.stream()
					.filter(c -> c.getNoOfAvailableCar() > 0)
					.forEach(car -> System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel() + " " + car.getNoOfAvailableCar()));
				} else if (choice == 4) {
					System.out.println("Thank you for choosingus.");
					break;
				} else {
					System.out.println("Please provide valid options.");
				}
			}
		}
	}

