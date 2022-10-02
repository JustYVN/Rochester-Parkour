package com.estore.api.estoreapi.controller;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.PassThroughSourceExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.Model.Product;
import com.estore.api.estoreapi.persistence.ProductDAO;
import com.fasterxml.jackson.annotation.JacksonInject;

@RestController
@RequestMapping("admin")
public class ProductController {
    
    private static final Logger LOG = Logger.getLogger(ProductController.class.getName());

    private ProductDAO productDao;
    
    public ProductController(ProductDAO productDao){
        this.productDao=productDao;
    }

    @GetMapping("product/{id}")
    public ResponseEntity<Product> GetSingleProduct(@PathVariable int id){
        LOG.info("GET /admin/product/ "+id);
        try{
                Product product = productDao.getProduct(id);
                if (product!=null){
                    return new ResponseEntity<Product>(product,HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
        }
        catch(IOException e){
                LOG.log(Level.SEVERE, e.getLocalizedMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 /**
     * Updates the {@linkplain Product newP} with the provided {@linkplain Product newP} object, if it exists
     * 
     * @param Product The {@link Product newP} to update
     * 
     * @return ResponseEntity with updated {@link Product newP} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Product> updateProduct(@PathVariable Product newP){
        LOG.info("PUT   /pruduct "+ newP);
        int id = newP.getID();
        try{
            if(productDao.getProduct(id) != null){
                productDao.updateProduct(newP);
                return new ResponseEntity<Product>(newP,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


    @GetMapping("product/")
    public ResponseEntity<Product[]> searchProduct(@RequestParam String name) {
        LOG.info("GET /admin/product/?Name="+name);

        try {
                Product[] products = productDao.findProduct(name);
                return new ResponseEntity<Product[]>(products, HttpStatus.OK);
        } 
        catch (IOException e) {
                LOG.log(Level.SEVERE,e.getLocalizedMessage());
                return new ResponseEntity<Product[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        LOG.info("DELETE /admin/product/"+id);
        try{
            if (productDao.getProduct(id)!=null){
                productDao.deleteProduct(id);
                return new ResponseEntity<>("The requested product was successfully Deleted",HttpStatus.OK);
            }
            else{
                return new ResponseEntity<String>("The Requested Product was not Found",HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products")
    public ResponseEntity<Product[]>getallInventory(){
        LOG.info("GET /admin/products");
        try{
                Product[] products= productDao.getProducts();
                return new ResponseEntity<Product[]>(products,HttpStatus.OK);
        }
        catch(IOException e){
                LOG.log(Level.SEVERE,e.getLocalizedMessage());
                return new ResponseEntity<Product[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
