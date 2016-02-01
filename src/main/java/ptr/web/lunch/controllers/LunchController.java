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
    public static final String FAIL = "Fail";
    private static final String SUCCESS = "OK";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    private SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);

    @Qualifier("lunchService")
    @Autowired
    private LunchService lunchService;



    @RequestMapping(value = "/get/lunchData", method = RequestMethod.GET,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<LunchItemDTO> getLunch() {
        logger.debug("=== getLunchData ===");
        List<LunchItemDTO> lunchItemData = new ArrayList<LunchItemDTO>();
        lunchItemData.add( new LunchItemDTO("meal1", 17.5));
        return lunchItemData;
    }

    @RequestMapping(value = "/create/role", method = RequestMethod.POST,
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody long addRole(@RequestBody Role role) {

        long Id = -1;
        logger.debug("=== addingRole : "+ role);
        Id = lunchService.addRole(role);
        logger.debug("=== roleId : "+ Id);
        return Id;
    }

    @RequestMapping(value = "/create/client", method = RequestMethod.POST,
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody long addClient(@RequestBody Client client) {

        long Id = -1;
        logger.debug("=== addingClient : "+ client);
        Id = lunchService.addClient(client);
        logger.debug("=== clientId : "+ Id);
        return Id;
    }

    @RequestMapping(value = "/{userName}/create/restaurant/", method = RequestMethod.POST,
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResultDTO addRestaurant( @PathVariable("userName") String userName, @RequestBody Restaurant restaurant) {

        long restaurantId = -1;
        logger.debug("=== addingRestaurant : "+ restaurant);
        if(lunchService.isAdminRole(userName)){
            restaurantId = lunchService.addRestaurant(restaurant);
            logger.debug("=== restaurantId : "+ restaurantId+" created successfully ");
            return new ResultDTO(String.valueOf(restaurantId),  LunchController.SUCCESS , "Restaurant : "+restaurant+" created successfully");
        } else {
            return new ResultDTO(userName, LunchController.FAIL, "User : "+userName+" does not have access rights to create restaurant" );
        }

    }

    @RequestMapping(value = "/{userName}/create/menuItem", method = RequestMethod.POST,
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResultDTO addMenuItem(@PathVariable("userName") String userName, @RequestBody MenuItem item) {
        long Id = -1;
        if(lunchService.isAdminRole(userName)){
            logger.debug("=== addingMenuItem : "+ item);
            Id = lunchService.addMenuItem(item);
            logger.debug("=== menuItemId : "+ Id + " successfully created");
            return new ResultDTO(String.valueOf(Id), LunchController.SUCCESS, "menuItemId : "+Id+" created successfully");
        } else {
            return new ResultDTO(userName, LunchController.FAIL,
                    "User : "+userName+" does not have access rights to create Menu Item" );
        }
    }

    @RequestMapping(value = "/{userName}/create/menuItemsList", method = RequestMethod.POST,
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
            return new ResultDTO(menuIds.toString(),  LunchController.SUCCESS , "menuItem list  : "+menuIds+" created successfully");
        } else {
            return new ResultDTO(userName, LunchController.FAIL, "User : "+userName+" does not have access rights to create Menu Item List" );
        }

    }

    @RequestMapping(value = "/{userName}/create/dailyMenu", method = RequestMethod.POST,
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResultDTO addDailyMenu(@PathVariable("userName") String userName, @RequestBody DailyMenuDTO menu) {

        if(lunchService.isAdminRole(userName)){
            try{
                logger.debug("=== addingDailyMenu : "+ menu);
                Long Id = lunchService.addDailyMenu(menu);
                logger.debug("=== DailyMenuId : "+ Id);
                return new ResultDTO(" Daily menu "+Id+" created ", LunchController.SUCCESS , menu.toString());
            } catch (ParseException pEx){
                throw new RuntimeException(pEx);
            }
        } else {
            return new ResultDTO(userName, LunchController.FAIL, "User : "+userName+" does not have access rights to create daily menu" );
        }
    }

    @RequestMapping(value = "/{userName}/create/dailyMenus", method = RequestMethod.POST,
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
                return new ResultDTO(createdDailyMenuIdList.toString(), LunchController.SUCCESS,  "Bulk create daily menu successfully completed " );
            } catch (ParseException pEx){
                throw new RuntimeException(pEx);
            }
        } else {
            return new ResultDTO(userName, LunchController.FAIL, "User : "+userName+" does not have access rights to create bulk daily menu" );
        }

    }


    @RequestMapping(value = "/create/dailyOrder", method = RequestMethod.POST,
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
            return new ResultDTO(order.toString(), LunchController.FAIL, "Check the input parameters, something is missing" );
        }


        message = (Id > -1) ? String.format("Client daily order has created %s", Id) : String.format(message, Id);
        return new ResultDTO(order.toString(), LunchController.SUCCESS, message );
    }


    @RequestMapping(value = "/update/clientDailyOrder", method = RequestMethod.POST,
            consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody long updateClientDailyOrder(@RequestBody ClientOrderDTO order) throws ParseException{

        long Id = -1;
        logger.debug("=== addingClientDailyOrder : "+ order);
        if(order != null){
            order.setParsedDate(sdf.parse(order.getOrderDate()));
            Id = lunchService.updateClientDailyOrder(order);
            logger.debug("=== ClientDailyOrderId : "+ Id+" has been updated ");
        }
        return Id;
    }


    @RequestMapping(value = "/get/dailyMenu", method = RequestMethod.GET, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody DailyMenuDTO getDailyMenu(@RequestParam(value = "restaurantName", required = false) String restaurantName,
                                                   @RequestParam(value = "menuDate", required = false)  @DateTimeFormat(pattern=YYYY_MM_DD) Date inputDate ) throws ParseException {

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