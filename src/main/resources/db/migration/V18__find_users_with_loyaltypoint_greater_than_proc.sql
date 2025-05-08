DELIMITER $$

CREATE PROCEDURE findUsersWithLoyaltyPointGreaterThan(
    loyaltyPoint INT
)
BEGIN
    SELECT u.id, u.email FROM users u
    LEFT JOIN profiles p ON p.id = u.id
    WHERE p.loyalty_points > loyaltyPoint
    ORDER BY u.email;
END $$

DELIMITER ;