package com.ar.patient.service;

import java.io.Serializable;
import java.util.List;

public class CommandModel implements Serializable {
    public String id;
    public String input;
    public int pause;
    public int is_default = 0;
    public List<String> response;
}
