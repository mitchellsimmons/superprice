INSERT INTO Category (ParentCategoryID, Name) VALUES
    (NULL, 'Fruit & Vegetables'), -- 1
    (NULL, 'Poultry, Meat & Seafood'), -- 2
    (NULL, 'Bakery'), -- 3
    (NULL, 'Dairy, Egg & Fridge'), -- 4
    (NULL, 'Pantry'), -- 5
    (NULL, 'Snacks'), -- 6
    (NULL, 'Freezer'), -- 7
    -- Fruit & Vegetables
    (1, 'Fruit'), -- 8
    (1, 'Vegetables'), -- 9
    (1, 'Salad'), -- 10
    (8, 'Bananas'), -- 11
    (8, 'Apples'), -- 12
    (8, 'Citrus'), -- 13
    (8, 'Grapes'), -- 14
    (8, 'Melons'), -- 15
    (8, 'Berries'), -- 16
    (9, 'Potatoes & Pumpkins'), -- 17
    (9, 'Carrots & Roots'), -- 18
    (9, 'Mushrooms'), -- 19
    (9, 'Tomatoes'), -- 20
    (10, 'Herbs'), -- 21
    (10, 'Salad Bags'), -- 22
    -- Poultry, Meat & Seafood
    (2, 'Poultry'), -- 23
    (2, 'Meet'), -- 24
    (2, 'Seafood'), -- 25
    (23, 'Chicken'), -- 26
    (23, 'Turkey'), -- 27
    (24, 'Beef'), -- 28
    (24, 'Lamb'), -- 29
    (24, 'Pork'), -- 30
    (25, 'Fish'), -- 31
    (25, 'Crab & Lobster'), -- 32
    (25, 'Oysters'), -- 33
    (25, 'Prawns'), -- 34
    -- Bakery
    (3, 'Freshly Baked'), -- 35
    (3, 'Packaged Bread & Cakes'), -- 36
    (35, 'Bread'), -- 37
    (35, 'Rolls'), -- 38
    (35, 'Pastries'), -- 39
    (36, 'Packaged Bread'), -- 40
    (36, 'Cakes'), -- 41
    (36, 'Rolls & Bagels'), -- 42
    -- Dairy, Eggs & Fridge
    (4, 'Dairy'), -- 43
    (4, 'Eggs'), -- 44
    (4, 'Fridge'), -- 45
    (43, 'Milk'), -- 46
    (43, 'Cheese'), -- 47
    (43, 'Cream'), -- 48
    (44, 'Freerange Eggs'), -- 49
    (44, 'Cage Eggs'), -- 50
    (45, 'Dips'), -- 51
    (45, 'Margarine'), -- 52
    -- Pantry
    (5, 'Tea & Coffee'), -- 53
    (5, 'Baking'), -- 54
    (5, 'Pasta, Rice & Grains'), -- 55
    (53, 'Tea'), -- 56
    (53, 'Coffee'), -- 57
    (54, 'Flour'), -- 58
    (54, 'Sugar'), -- 59
    (55, 'Pasta'), -- 60
    (55, 'Rice'), -- 61
    (55, 'Grains'), -- 62
    -- Snacks
    (6, 'Confectionary'), -- 63
    (6, 'Chips'), -- 64
    (6, 'Biscuits'), -- 65
    (63, 'Chocolate Bars'), -- 66
    (63, 'Chocolate Blocks'), -- 67
    (64, 'Potato Chips'), -- 68
    (64, 'Corn Chips'), -- 69
    (65, 'Biscuits & Cookies'), -- 70
    (65, 'Crackers'), -- 71
    -- Freezer
    (7, 'Ice Cream'), -- 72
    (7, 'Frozen Meals'), -- 73
    (7, 'Frozen Fruit'), -- 74
    (72, 'Tubs'), -- 75
    (72, 'Sticks & Cones'), -- 76
    (73, 'Frozen Pizza'), -- 77
    (73, 'Dumplings'), -- 78
    (73, 'Meat Pies'), -- 79
    (74, 'Blueberries'), -- 80
    (74, 'Raspberries'); -- 81
