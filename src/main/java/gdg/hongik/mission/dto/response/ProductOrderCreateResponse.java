package gdg.hongik.mission.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ProductOrderCreateResponse {


    @Schema(description = "상품 전체 가격", example = "20000")
    private Long totalPrice;


    @Schema(description = "주문 상품 목록")
    private List<Item> item;


    public ProductOrderCreateResponse(List<Item> item) {
        this.item = item;
    }


    @Data
    public static class Item {

        @Schema(description = "상품 이름", example = "apple")
        private String name ;

        @Schema(description = "상품 갯수", example = "20")
        private Long cnt;

        @Schema(description = "상품 가격", example = "1000")
        private Long price ;


        @Builder
        public Item(Long price, String name, Long cnt) {

            this.price = price;
            this.name = name;
            this.cnt = cnt;

        }

    }

}
