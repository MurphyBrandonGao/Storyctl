package fscut.manager.demo.dao;

import fscut.manager.demo.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Integer> {

    /**
     * 将消息保存至customer_message表中
     * @param messageId 消息id
     * @param customerId 用户id
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "insert into customer_message (message_id, customer_id) select ?1,?2 from DUAL where not exists (select customer_id from customer_message where message_id = ?1 and customer_id = ?2)", nativeQuery = true)
    void addCustomerMessage(@Param("message_id") Integer messageId, @Param("customer_id") Integer customerId);

    /**
     * 根据用户id查找未读消息id
     * @param customerId 用户id
     * @return 未读消息id列表
     */
    @Query(value = "select distinct message_id from customer_message where customer_id = ?1 and checked = 0",nativeQuery = true)
    List<Integer> getUnreadMessageId(@Param("customer_id") Integer customerId);

    /**
     * 根据用户id查找消息id列表
     * @param customerId 用户id
     * @return 消息id列表
     */
    @Query(value = "select distinct message_id from customer_message where customer_id = ?1",nativeQuery = true)
    List<Integer> getMessageId(@Param("customer_id") Integer customerId);

    /**
     * 根据用户名查找消息id列表
     * @param username 用户名
     * @return 消息id列表
     */
    @Query(value = "select distinct message_id from customer_message as cm left join customer as c on cm.customer_id = c.id where c.username = ?1",nativeQuery = true)
    List<Integer> getMessageId(@Param("username") String username);

    /**
     * 根据消息id列表查找消息
     * @param messageId 消息id
     * @return 消息列表
     */
    List<Message> findMessagesByMessageIdIn(List<Integer> messageId);

    /**
     * 根据消息id和用户id设置消息已读
     * @param messageId 消息id
     * @param customerId 用户id
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "update customer_message set checked = 1 where message_id = ?1 and customer_id = ?2", nativeQuery = true)
    void readMessage(Integer messageId, Integer customerId);

    /**
     * 根据用户id查找未读消息条数
     * @param customerId 用户id
     * @return 未读信息条数
     */
    @Query(value = "select count(*) from  customer_message where customer_id = ?1 and checked = 0", nativeQuery = true)
    Integer getUnreadMessageNum(Integer customerId);

    /**
     * 根据用户名查找未读消息条数
     * @param username 用户名
     * @return 未读信息条数
     */
    @Query(value = "select count(*) from  customer_message as cm left join  customer as c on cm.customer_id = c.id where c.username = ?1 and cm.checked = 0", nativeQuery = true)
    Integer getUnreadMessageNum(String username);

    /**
     * 根据消息id和用户id删除消息
     * @param messageId 消息id
     * @param customerId 用户id
     */
    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query(value = "delete from customer_message where message_id = ?1 and customer_id = ?2", nativeQuery = true)
    Integer deleteCustomerMessage(Integer messageId, Integer customerId);

}