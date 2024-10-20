package com.example.demo.service;

import com.example.demo.entity.Orders;
import com.example.demo.entity.Room;
import com.example.demo.entity.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    public ByteArrayInputStream usersToExcel(List<User> users) throws IOException {
        String[] COLUMNs = {"ID", "Username", "Email", "Verified", "Role"};

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Users");

            // Header
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < COLUMNs.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(COLUMNs[i]);
            }

            // Data
            int rowIdx = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getUsername());
                row.createCell(2).setCellValue(user.getEmail());
                row.createCell(3).setCellValue(user.isVerified());
                row.createCell(4).setCellValue(user.getRole().name());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream roomsToExcel(List<Room> rooms) throws IOException {
        String[] COLUMNs = {"ID", "Room No", "type", "price", "available"};

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Rooms");

            // Header
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < COLUMNs.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(COLUMNs[i]);
            }

            // Data
            int rowIdx = 1;
            for (Room room : rooms) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(room.getId());
                row.createCell(1).setCellValue(room.getRoomNo());
                row.createCell(2).setCellValue(room.getType());
                row.createCell(3).setCellValue(room.getPrice());
                row.createCell(4).setCellValue(room.isAvailable());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream ordersToExcel(List<Orders> orders) throws IOException {
        String[] COLUMNs = {"ID", "Date", "price", "description", "order owner username"};

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Orders");

            // Header
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < COLUMNs.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(COLUMNs[i]);
            }

            // Data
            int rowIdx = 1;
            for (Orders room : orders) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(room.getId());
                row.createCell(1).setCellValue(room.getDate());
                row.createCell(2).setCellValue(room.getPrice());
                row.createCell(3).setCellValue(room.getDescription());
                row.createCell(4).setCellValue(room.getUser().getUsername());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
