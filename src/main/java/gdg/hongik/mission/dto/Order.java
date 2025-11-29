package gdg.hongik.mission.dto;



import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
public class Order {
    private List<Item> items;


    @Getter
    @Setter
    public static class Item{
        private String name;
        private Long stock;

    }

    private long id;
}
