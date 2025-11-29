package gdg.hongik.mission.controller;



import gdg.hongik.mission.dto.request.OrderCreateRequest;
import gdg.hongik.mission.dto.response.OrderCreateResponse;
import gdg.hongik.mission.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.web.servlet.function.ServerResponse.created;

/**
 * order 클래스
 * 
 */

@RestController
@RequestMapping("order")
@Tag(name="Order",description = "Order API")
public class OrderController {


    private final ProductService productService;

    public OrderController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * @param request 요청하기
     * @return 반환 목록 없음
     */


    @PatchMapping
    @Operation(summary = "주문 등록", description = "주문 정보를 생성합니다")
    @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json",
            examples = {@ExampleObject(
                    name = "주문정보 생성 성공",
                    value = """
                    {
                        "totalPrice" : 40000,
                        "cart" : 
                        [
                            {
                                "name" : "apple",
                                "cnt" : 20,
                                "price" : 1000
                            },
                            {
                                "name"  : "water",
                                "cnt"   : 20,
                                "price" : 1000
                            }
                        ]
                    }
                    """)
    }))
    public ResponseEntity<OrderCreateResponse> createOrder(@RequestBody OrderCreateRequest request){

        //product의 재고랑 연결하기
        OrderCreateResponse result = productService.orderCreate(request);

        return ResponseEntity.ok(result);


    }
}
