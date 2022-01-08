package com.carm.web.controller;

import com.carm.entity.Order;
import com.carm.entity.SparePart;
import com.carm.entity.Vehicle;
import com.carm.entity.Worker;
import com.carm.service.OrderService;
import com.carm.service.SpareService;
import com.carm.service.VehicleService;
import com.carm.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class CarMaintenanceController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SpareService spareService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private WorkerService workerService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    protected String main() {
        return "index";
    }

    @RequestMapping(path = "/box1", method = RequestMethod.GET)
    protected String box1(Model model) {
        List<Order> orders = null;
        try {
            orders = orderService.findAll();
            if(orders.isEmpty()){
                throw new SQLException();
            }
            model.addAttribute("orders", orders);
        } catch (Exception throwables) {
            model.addAttribute("error", "No Orders");
            throwables.printStackTrace();
        }

        return "box1";
    }

    @RequestMapping(path = "/box1", method = RequestMethod.POST, params = "generate")
    protected String generateTestData(Model model) throws SQLException {
        vehicleService.generateVehicles();
        workerService.generateWorker();
        spareService.generateSpareParts();
        orderService.addRandomOrders();

        model.addAttribute(orderService.findAll());
        return "redirect:/box1";
    }

    @RequestMapping(path = "/box1", method = RequestMethod.POST, params = "delete")
    protected String deleteAll() throws SQLException {
            orderService.deleteData();
            workerService.delete();
            vehicleService.delete();
            spareService.delete();
        return "redirect:/box1";
    }

    @RequestMapping(path = "/new", method = RequestMethod.GET)
    protected String newOrder(Model model) throws SQLException {

        if (vehicleService.findAllVehiclesByVin().isEmpty()) {
            // Create a default vehicle
            Vehicle vehicle = Vehicle.builder().
                    model("default")
                    .owner("owner")
                    .VIN("000000000000")
                    .build();
            vehicleService.add(vehicle);
        }
        if (workerService.findAllWorkers().isEmpty()) {
            // Create a default worker
            Worker worker = Worker.builder().
                    firstName("0worker")
                    .lastName("0worker")
                    .build();
            workerService.add(worker);
        }

        if (spareService.getPartsByName().isEmpty()) {
            // Create a default part
            SparePart sparePart = SparePart.builder().
                    number(0)
                    .cost(0)
                    .name("default")
                    .build();

            spareService.add(sparePart);
        }
        model.addAttribute("vins", vehicleService.findAllVehiclesByVin());
        model.addAttribute("workers", workerService.findAllWorkers());
        model.addAttribute("parts", spareService.getPartsByName());

        return "new";
    }

    @RequestMapping(path = "/vehicle", method = RequestMethod.GET)
    protected String addVehicle() {
        return "vehicle";
    }

    @RequestMapping(path = "/worker", method = RequestMethod.GET)
    protected String addWoker() {
        return "worker";
    }

    @RequestMapping(path = "/worker", method = RequestMethod.POST)
    protected String woker(@RequestParam(value = "fname", required = false) String fname,
                           @RequestParam(value = "lname", required = false) String lname) throws SQLException {
        Worker worker = Worker.builder().
                firstName(fname)
                .lastName(lname)
                .build();

        workerService.add(worker);

        return "worker";
    }

    @RequestMapping(path = "/vehicle", method = RequestMethod.POST)
    protected String vehicle(@RequestParam(value = "model", required = false) String model,
                             @RequestParam(value = "owner", required = false) String owner,
                             @RequestParam(value = "vin", required = false) String vin) throws SQLException {

        Vehicle vehicle = Vehicle.builder().
                model(model)
                .owner(owner)
                .VIN(vin)
                .build();

        vehicleService.add(vehicle);

        return "vehicle";
    }

    @RequestMapping(path = "/parts", method = RequestMethod.GET)
    protected String parts() throws SQLException {
        return "parts";
    }

    @RequestMapping(path = "/parts", method = RequestMethod.POST)
    protected String addPart(@RequestParam(value = "number", required = false) String number,
                             @RequestParam(value = "cost", required = false) String cost,
                             @RequestParam(value = "name", required = false) String name) throws SQLException {

        SparePart sparePart = SparePart.builder().
                number(Integer.parseInt(number))
                .cost(Integer.parseInt(cost))
                .name(name)
                .build();

        spareService.add(sparePart);

        return "parts";
    }

    @RequestMapping(path = "/new", method = RequestMethod.POST)
    protected String addOrder(@RequestParam(value = "vehicle") String vehicle,
                           @RequestParam(value = "parts") String parts,
                              @RequestParam(value = "worker") String worker,
                              @RequestParam(value = "complexity") String complexity,
                              @RequestParam(value = "price") String price,
                              @RequestParam(value = "start") String start,
                              @RequestParam(value = "end") String end) throws SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);


        Order order = Order.builder().
                vehicle(vehicleService.findByVin(vehicle))
                .worker(workerService.findWorker(worker))
                .sparePart(spareService.findPartByName(parts))
                .price(Integer.parseInt(price))
                .complexity(Integer.valueOf(complexity))
                .startDate(startDate.atStartOfDay())
                .endDate(endDate.atStartOfDay())
                .build();

        orderService.add(order);

        return "redirect:/box1";
    }

    @RequestMapping(path = "/remove", method = RequestMethod.GET)
    protected String remove(Model model) throws SQLException {
        model.addAttribute("orders", orderService.findAllOrdersById());

        return "remove";
    }

    @RequestMapping(path = "/remove", method = RequestMethod.POST)
    protected String removeApply(@RequestParam (value = "remove") String id) throws SQLException {
        orderService.removeorder(id);
        return "redirect:/box1";
    }
}
