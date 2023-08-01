package hi.cosmonaut.hourly.tool.mapping;

public interface Mapping <I, O>{
    O perform(I input);
}
