package gdg.hongik.mission.dto;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Product 객체 생성
 * 주의!! Table name만 products 복수형임
 */

@Getter
@Setter
@Entity
@Table(name = "products")
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id")
    private Long productId;


    @Column(name = "p_name", length = 100)
    private String name;


    @Column(name = "p_price", length = 100)
    private Long price;


    @Column(name = "p_stock", length = 100)
    private Long stock;

    @Builder
    public Product(String name, Long price, Long stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }


    public void updateProduct(Long cnt){
        if(cnt != null) {
            this.stock = this.stock + cnt;
        }
    }
    public void updateOrderProduct(Long cnt){
        if(cnt != null) {
            this.stock = this.stock - cnt;
        } else{
            System.out.println("There are not enough stock");
        }
    }
}
