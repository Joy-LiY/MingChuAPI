package com.example.mingchuapi.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AndmuResult {
	private String code;

	private String result;

	private Map<String, String> statusMap = new HashMap<>();
}
