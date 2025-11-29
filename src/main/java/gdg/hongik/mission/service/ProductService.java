package gdg.hongik.mission.service;


import gdg.hongik.mission.dto.Product;
import gdg.hongik.mission.dto.request.OrderCreateRequest;
import gdg.hongik.mission.dto.request.ProductCreateRequest;
import gdg.hongik.mission.dto.request.ProductUpdateRequest;
import gdg.hongik.mission.dto.response.OrderCreateResponse;
import gdg.hongik.mission.dto.response.ProductDeleteResponse;
import gdg.hongik.mission.dto.response.ProductGetResponse;
import gdg.hongik.mission.dto.response.ProductUpdateResponse;
import gdg.hongik.mission.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 
 * 서비스 계층 비즈니스 로직을 담당하는 계층
 * 
 */
@Service
public class ProductService {


    /**
     * DB를 사용하기 위해 의존성 주입
     */
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }


    /**
     * 재고 검색 비즈니스 로직
     * 람다 함수로 값 찾음 
     * 널 or 실제값 반환
     * @param productName
     * @return
     */
    @Transactional
    public ProductGetResponse getProduct (String productName){

        //코드 중복임
        /*
        Map.Entry<Long,Product> find = repository.entrySet().stream()
                .filter(p -> productName.equals(p.getValue().getName()))
                .findFirst()
                .orElse(null);
        */

        Product find = repository.findByName(productName);
        if (find == null){
            throw new RuntimeException("존재하지 않는 상품명 입니다");
        }



        ProductGetResponse pg = ProductGetResponse.builder()
                .id(find.getProductId())
                .stock(find.getStock())
                .price(find.getPrice())
                .name(find.getName())
                .build();

        return pg;
    }


    /**
     * 재고 생성 비즈니스
     *
     * @param request
     */
    @Transactional
    public void postProduct(ProductCreateRequest request) {

        Product ex = repository.findByName(request.getName());
        if( ex != null){
            throw new RuntimeException("이미 존재하는 상품입니다");
        }

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .build(); //builder 패턴

        repository.save(product);
    }



    /**
     * 물건 업데이트 서비스 로직
     * @param findId
     * @param request
     * @return
     */
    @Transactional
    public ProductUpdateResponse updateProduct(
            Long findId,
            ProductUpdateRequest request) {

        Product product = repository.findById(findId);

        if(product == null) {
            throw new RuntimeException("존재 하지 않는 상품입니다");
        }


        product.updateProduct(request.getCnt());

        ProductUpdateResponse pr = ProductUpdateResponse.builder()
                .name(product.getName())
                .stock(product.getStock())
                .build();

        return pr;

    }

    /**
     * 물품 삭제 서비스 로직
     * @param deleteNames
     * @return
     */
    @Transactional
    public ProductDeleteResponse deleteProducts(List<String> deleteNames) {

        // 응답 객체와 남은 물품 리스트 초기화
        ProductDeleteResponse response = new ProductDeleteResponse();
        List<ProductDeleteResponse.Item> remainingItems = new ArrayList<>();

        //
        for(String deName :deleteNames ){
            Product product = repository.findByName(deName);
            if( product == null){
                System.out.println("존재하지 않는 상품명 입니다");
                continue;
            }

            repository.deleteByName(product.getName());

        }

        for(Product remainProduct :  repository.findAll()){

            ProductDeleteResponse.Item item = new ProductDeleteResponse.Item();
            item.setName(remainProduct.getName());
            item.setStock(remainProduct.getStock());
            remainingItems.add(item);

        }




        // 응답 구성 및 반환
        response.setItems(remainingItems);
        return response;
    }

    /**
     * Order 컨트롤러에서 연결
     * 물품 차감 , 이외의 에러처리 구현 x
     * @param request
     * @return
     */
    @Transactional
    public OrderCreateResponse orderCreate(OrderCreateRequest request) {

        //리턴할 item 리스트
        List<OrderCreateResponse.Item> or = new ArrayList<>();
        Long totalPrice = 0L;


        //상품 이름으로 찾기
        for(OrderCreateRequest.Item item : request.getItems()){
            String itemName = item.getName();
            Product product = repository.findByName(itemName);

            if(product == null){
                System.out.println("존재하지 않는 상품명 입니다, orderCreate");
                continue;
            }



            //구매할 상품 갯수
            Long n = item.getCnt();

            //실제값 변경
            //충분하게 있는지 확인 x
            product.updateOrderProduct(n);


            //리턴할 itemDTO 한개 생성 후 리턴
            OrderCreateResponse.Item pg = OrderCreateResponse.Item.builder()
                    .price(product.getPrice() * n)
                    .name(product.getName())
                    .cnt(n)
                    .build();
            or.add(pg);

            totalPrice += product.getPrice() * n;
        }

        OrderCreateResponse result = new OrderCreateResponse() ;
        result.setTotalPrice(totalPrice);
        result.setItem(or);


        return result;
    }


}
