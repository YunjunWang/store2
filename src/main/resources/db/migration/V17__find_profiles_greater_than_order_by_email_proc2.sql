DELIMITER $$

CREATE PROCEDURE findProfilesGreaterThan2(
    loyaltyPoint BIGINT
)
BEGIN
    SELECT p.id, p.loyalty_points, u.email FROM profiles p
    JOIN users u ON u.id = p.id
    WHERE p.loyalty_points > loyaltyPoint
    ORDER BY u.email;
END $$

DELIMITER ;