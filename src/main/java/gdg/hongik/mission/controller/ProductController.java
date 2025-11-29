package gdg.hongik.mission.controller;



import gdg.hongik.mission.dto.Product;
import gdg.hongik.mission.dto.request.ProductCreateRequest;
import gdg.hongik.mission.dto.request.ProductDeleteRequest;
import gdg.hongik.mission.dto.request.ProductUpdateRequest;
import gdg.hongik.mission.dto.response.ProductDeleteResponse;
import gdg.hongik.mission.dto.response.ProductGetResponse;
import gdg.hongik.mission.dto.response.ProductUpdateResponse;
import gdg.hongik.mission.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

import static ch.qos.logback.core.joran.spi.ConsoleTarget.findByName;


/**
 * Product 컨트롤러
 */
@RestController
@RequestMapping("product")
@Tag(name ="Product-Controller" ,description = "Product API")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 재고 검색 컨트롤러
     * @param name 파라미터
     * 1. 상품을 찾을 수 없을때 NOT_FOUND
     * 2. 상품 찾으면 리턴
     * @return 리턴값 재고
     */

    @GetMapping("/{name}")
    @Operation(summary = "재고 검색", description = "재고 정보를 검색합니다. 사용자 관리자 모두 사용 가능")
    @ApiResponse(responseCode = "200",content = @Content(mediaType = "application/json",
            examples = {@ExampleObject(
                    name="재고 검색 성공",
                    value = """
                            {
                                "id" : 1,
                                "name" : "apple",
                                "price" : 1000,
                                "stock" : 100                            
                            }
                            """
            )}))
    public ResponseEntity<?> getProduct(@PathVariable String name){
        //컨트롤러 계층
        //사용자요청을 처리
        //컨트롤러에 부합하지 않음
        /*
        boolean ex = productService.findByName(name);
        if(ex == false ){
            String errorMessage = "요청하신 상품을 찾을 수 없습니다. name: " + name;

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(errorMessage); // String을 본문에 담아 반환
        }
        */ 
        ProductGetResponse item = productService.getProduct(name);

        return ResponseEntity.ok(item);

    }


    /**
     * 재고 등록 매소드
     * 1. 중복 확인
     * 2. 재고 등록
     * @param request 바디 값 요청
     * @return
     */
    @PostMapping
    @Operation(summary = "재고 등록", description = "재고를 등록합니다. 관리자만 사용 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json",
                    examples = {@ExampleObject(
                            name = "재고 등록 성공",
                            value = """
                            {
                                "name" : "apple",
                                "stock" : 100,
                                "price" : 1000
                            }
                            """)
                    })),
            @ApiResponse(responseCode = "400", content = @Content( mediaType = "application/json",
                    examples = {@ExampleObject(
                            name = "재고등록 실패 - 이미 존재하는 상품",
                            value = """
                            {
                                "message" : "이미 존재하는 상품입니다"
                            }
                            """
                    )
                    }))
    })

    public ResponseEntity<Void> createProduct(@RequestBody ProductCreateRequest request){


        // 중복인지 확인
        // 이건 컨트롤러에 맞지 않은 코드임
        /*
        boolean ex= productService.findByName(request.getName());
        if( ex == true ) {


            String errorMessage = "이미 존재하는 상품명 입니다. name: " + request.getName();

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(errorMessage); // String을 본문에 담아 반환


        }
        */


        //물품 만들기
        productService.postProduct(request);
        
        return ResponseEntity.created(URI.create("product")).build();
    }

    /**
     * 재고 수정
     * 1. 재고 확인
     * 2. 존재하면 업데이트
     * @param id 파라미터
     * @param request
     * @return
     */
    @PatchMapping("/{id}")
    @Operation(summary = "재고 수정" , description = "재고를 수정합니다, 관리자만 가능")
    @ApiResponse(responseCode = "200" , content = @Content(mediaType ="application/json",
        examples = {@ExampleObject(
                name ="재고 수정 완료",
                value = """
                        {
                            "name"  : "apple",
                            "stock"   : 110
                        }
                        """
        )}))

    public ResponseEntity<ProductUpdateResponse> updateProduct(
            @PathVariable Long id, @RequestBody ProductUpdateRequest request){
        
        //업데이스 서비스계층 전달
        ProductUpdateResponse result = productService.updateProduct(id,request);

        return ResponseEntity.ok(result);
    }

    /**
     * 물품 삭제 메소드
     * @param request
     * @return
     */
    @DeleteMapping
    @Operation(summary = "물품 삭제", description = "관리자만 사용 가능")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            examples = {@ExampleObject(
                    name = "물품 삭제 성공",
                    value = """
                            {
                                "items" :
                                [
                                    {
                                     "name" : "milk",
                                     "stock" : 20
                                    },
                                    {
                                     "name" : "orange",
                                     "sotck" : 20
                                    }
                                ]
                            }
                            """
            )}))
    public ResponseEntity<ProductDeleteResponse> deleteProduct(
            @RequestBody ProductDeleteRequest request){



        //이름들 호출
        List<String> deleteNames = new ArrayList<>();
        for(String findName : request.getName()){
            deleteNames.add(findName);
        }


        // 2. 서비스 로직 호출 및 응답 객체 받기
        // 서비스는 삭제 후 남은 목록을 포함한 ProductDeleteResponse 객체를 반환함
        ProductDeleteResponse response = productService.deleteProducts(deleteNames);

        return ResponseEntity.ok(response);
    }


}
