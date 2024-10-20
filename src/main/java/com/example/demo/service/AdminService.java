package com.example.demo.service;

import com.example.demo.entity.Orders;
import com.example.demo.entity.Room;
import com.example.demo.entity.User;
import com.example.demo.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final ExcelService excelService;
    private final RoomService roomService;
    private final OrdersService ordersService;

    public ByteArrayInputStream getAllUserInFile() throws IOException {
        List<User> allUsers = userService.getAllUsers();
        return excelService.usersToExcel(allUsers);
    }

    public ByteArrayInputStream getAllRoomsInFile() throws IOException {
        List<Room> rooms = roomService.getAllRooms();
        return excelService.roomsToExcel(rooms);

    }

    public ByteArrayInputStream getAllOrdersInFile() throws IOException {
        List<Orders> orders = ordersService.getAllOrders();
        return excelService.ordersToExcel(orders);
    }
}
