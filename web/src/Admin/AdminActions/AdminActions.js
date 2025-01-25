const createCategory = (C_name, C_isPromoted, C_movies) => {
    const categoryData = {
        name: C_name,
        isPromoted: C_isPromoted,
        movies: C_movies,
    };

    return fetch("http://localhost:12345/api/categories/", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "userId": "6793f41b8221f4dda02b7e63",
        },
        body: JSON.stringify(categoryData)
    })
    .then(response => {
        if (response.status === 201) {
            alert("Category created successfully!");
            console.log("Category created successfully");
            return true;    
        } else {
            alert("Name is not available, try another one");
            throw new Error(`Error: ${response.status} - ${response.statusText}`);
        }
    })
    .catch(error => {
        console.error("Error during category creation:", error);
        return false;
    }); 
};

const getAllCategories = (userId) => {
    return fetch("http://localhost:12345/api/categories/", {
        method: "GET",
        headers: {
            "userId": userId,
        }
    })
    .then(response => response.json())
    .then(data => {
        console.log("Categories fetched successfully:", data);
        return data.map(category => category.name);
    })
    .catch(error => {
        console.error("Error fetching categories:", error);
        return [];
    });
};



export { createCategory, getAllCategories };
