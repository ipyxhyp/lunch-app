package ptr.web.lunch.controllers;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ptr.web.lunch.dto.ClientOrderDTO;
import ptr.web.lunch.dto.DailyMenuDTO;
import ptr.web.lunch.dto.LunchItemDTO;
import ptr.web.lunch.dto.ResultDTO;
import ptr.web.lunch.dto.TestDTO;
import ptr.web.lunch.model.Client;
import ptr.web.lunch.model.DailyMenu;
import ptr.web.lunch.model.MenuItem;
import ptr.web.lunch.model.Restaurant;
import ptr.web.lunch.model.Role;
import ptr.web.lunch.services.LunchService;

//import javax.ws.rs.core.Response
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
@Scope("request")
@RequestMapping("/lunch")
public class LunchController {

    private static final Logger logger = Logger.getLogger(LunchController.class);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Qualifier("lunchService")
    @Autowired
    private LunchService lunchService;



    @RequestMapping(value = "getLunchData", method = RequestMethod.GET,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<LunchItemDTO> getLunch() {
        logger.debug("=== getLunchData ===");
        List<LunchItemDTO> lunchItemData = new ArrayList<LunchItemDTO>();
        lunchItemData.add( new LunchItemDTO("meal1", 17.5));
        return lunchItemData;
    }

    @RequestMapping(value = "/createRole", method = RequestMethod.POST,
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody long addRole(@RequestBody Role role) {

        long Id = -1;
        logger.debug("=== addingRole : "+ role);
        Id = lunchService.addRole(role);
        logger.debug("=== roleId : "+ Id);
        return Id;
    }

    @RequestMapping(value = "createClient", method = RequestMethod.POST,
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody long addClient(@RequestBody Client client) {

        long Id = -1;
        logger.debug("=== addingClient : "+ client);
        Id = lunchService.addClient(client);
        logger.debug("=== clientId : "+ Id);
        return Id;
    }

    @RequestMapping(value = "/{userName}/createRestaurant/", method = RequestMethod.POST,
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResultDTO addRestaurant( @PathVariable("userName") String userName, @RequestBody Restaurant restaurant) {

        long restaurantId = -1;
        logger.debug("=== addingRestaurant : "+ restaurant);
        if(lunchService.isAdminRole(userName)){
            restaurantId = lunchService.addRestaurant(restaurant);
            logger.debug("=== restaurantId : "+ restaurantId+" created successfully ");
            return new ResultDTO(String.valueOf(restaurantId),  "OK" , "Restaurant : "+restaurant+" created successfully");
        } else {
            return new ResultDTO(userName, "Fail", "User : "+userName+" does not have access rights to create restaurant" );      
        }
        
    }

    @RequestMapping(value = "/{userName}/createMenuItem", method = RequestMethod.POST,
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResultDTO addMenuItem(@PathVariable("userName") String userName, @RequestBody MenuItem item) {
        long Id = -1;
        if(lunchService.isAdminRole(userName)){
            logger.debug("=== addingMenuItem : "+ item);
            Id = lunchService.addMenuItem(item);
            logger.debug("=== menuItemId : "+ Id + " successfully created");
            return new ResultDTO(String.valueOf(Id),  "OK" , "menuItemId : "+Id+" created successfully");
        } else {
            return new ResultDTO(userName, "Fail", "User : "+userName+" does not have access rights to create Menu Item" );      
        }
    }

    @RequestMapping(value = "/{userName}/createMenuItemsList", method = RequestMethod.POST,
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResultDTO createMenuItemsList(@PathVariable("userName") String userName,
            @RequestBody Collection<LunchItemDTO> itemList) {
        List<Long> menuIds = new ArrayList<Long>(10);
        long Id = -1;
        if(lunchService.isAdminRole(userName)){
            logger.debug("=== createMenuItemsList : "+ itemList);
            for(LunchItemDTO menuItemDTO : itemList){
                Id = lunchService.addMenuItem(new MenuItem(menuItemDTO));
                logger.debug("=== created menuItemId : "+ Id);
                menuIds.add(Id);
            }
            return new ResultDTO(menuIds.toString(),  "OK" , "menuItem list  : "+menuIds+" created successfully");
        } else {
            return new ResultDTO(userName, "Fail", "User : "+userName+" does not have access rights to create Menu Item List" );      
        }
        
    }

    @RequestMapping(value = "/{userName}/createDailyMenu", method = RequestMethod.POST,
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResultDTO addDailyMenu(@PathVariable("userName") String userName, @RequestBody DailyMenuDTO menu) {
       
        if(lunchService.isAdminRole(userName)){
            try{
                logger.debug("=== addingDailyMenu : "+ menu);
                Long Id = lunchService.addDailyMenu(menu);
                logger.debug("=== DailyMenuId : "+ Id);
                return new ResultDTO(" Daily menu "+Id+" created ", "OK" , menu.toString());
            } catch (ParseException pEx){
                throw new RuntimeException(pEx);
            }       
        } else {
          return new ResultDTO(userName, "Fail", "User : "+userName+" does not have access rights to create daily menu" );      
        }                
    }

    @RequestMapping(value = "/{userName}/bulkCreateDailyMenus", method = RequestMethod.POST,
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResultDTO bulkCreateDailyMenus(@PathVariable("userName") String userName,
                @RequestBody Collection<DailyMenuDTO> menuList) {
        // TODO check if has role admin then addDailyMenu
        List<Long> createdDailyMenuIdList = new ArrayList<Long>(10);
        long Id = -1;
       if(lunchService.isAdminRole(userName)){
           try{
                for(DailyMenuDTO menu : menuList){
                    logger.debug("=== bulkCreateDailyMenus : "+ menuList);
                    Id = lunchService.addDailyMenu(menu);
                    logger.debug("=== DailyMenuId : "+ Id);
                    createdDailyMenuIdList.add(Id);
                }
                return new ResultDTO(createdDailyMenuIdList.toString(), "OK",  "Bulk create daily menu successfully completed " );
            } catch (ParseException pEx){
                throw new RuntimeException(pEx);
            }          
       } else {
           return new ResultDTO(userName, "Fail", "User : "+userName+" does not have access rights to create bulk daily menu" ); 
       }
       
    }


    @RequestMapping(value = "/createClientDailyOrder", method = RequestMethod.POST,
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResultDTO addClientDailyOrder(@RequestBody ClientOrderDTO order) throws ParseException {
        long  Id = -1;
        String message = "Client daily order is in progress %s";
        logger.debug("=== addingClientDailyOrder : "+ order);
        if(StringUtils.isNotBlank(order.getRestaurantName())
                && StringUtils.isNotBlank(order.getUserName())
                && StringUtils.isNotBlank(order.getOrderDate())
                && StringUtils.isNotBlank(order.getMenuItemName())){

            order.setParsedDate(sdf.parse(order.getOrderDate()));
            Id = lunchService.addClientDailyOrder(order);
            logger.debug("=== ClientDailyOrderId : "+ Id);
        } else {
           return new ResultDTO(order.toString(), "Fail", "Check the input parameters, something is missing" );

        }


        message = (Id > -1) ? String.format("Client daily order has created %s", Id) : String.format(message, Id);
        return new ResultDTO(order.toString(), "OK", message );
    }


    @RequestMapping(value = "/updateClientDailyOrder", method = RequestMethod.POST,
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody long updateClientDailyOrder(@RequestBody ClientOrderDTO order) throws ParseException{

        long Id = -1;
        logger.debug("=== addingClientDailyOrder : "+ order);
        if(order != null){
            order.setParsedDate(sdf.parse(order.getOrderDate()));
            Id = lunchService.updateClientDailyOrder(order);
            logger.debug("=== ClientDailyOrderId : "+ Id+" has been updated ");
            logger.debug("=== ClientDailyOrderId : "+ Id);
        }

        return Id;
    }


    @RequestMapping(value = "getDailyMenu", method = RequestMethod.GET, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody DailyMenuDTO getDailyMenu(@RequestParam(value = "restaurantName", required = false) String restaurantName,
            @RequestParam(value = "menuDate", required = false)  @DateTimeFormat(pattern="yyyy-MM-dd") Date inputDate ) throws ParseException {

        DailyMenuDTO result = new DailyMenuDTO();
        logger.debug("=== getDailyMenu params : "+ inputDate + " name : "+restaurantName);
        DailyMenu dailyMenu = lunchService.getDailyMenu(restaurantName, inputDate);
        if(dailyMenu != null){
          result.setMenuDate(sdf.format(dailyMenu.getMenuDate()));
          result.setRestaurantName(dailyMenu.getRestaurantId().getName());
          result.setLunchItems(dailyMenu.getMenuItems());
        }
        return result;
    }

}