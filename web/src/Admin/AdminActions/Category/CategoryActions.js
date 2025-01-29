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
            "userId": localStorage.getItem('userId'),
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

const getAllCategories = () => {
    return fetch("http://localhost:12345/api/categories/", {
        method: "GET",
        headers: {
            "userId": localStorage.getItem('userId'),
        }
    })
    .then(response => response.json())
    .then(data => {
        // console.log("Categories fetched successfully:", data);
        return data.map((category) => category._id);
    })
    .catch(error => {
        console.error("Error fetching categories:", error);
        return [];
    });
};

const getCategoryById = (categoryId) => {
    return fetch(`http://localhost:12345/api/categories/${categoryId}`, {
        method: "GET",
        headers: {
            "userId": localStorage.getItem('userId'),
        }
    })
    .then((response) => {
        if (response.status === 200) {
            return response.json(); 
        } else {
            alert(`ID is not available, try another one - ${response.status}`);
            throw new Error(`Error: ${response.status} - ${response.statusText}`);
        }
    })
    .then((data) => {
        if (!data) {
            // console.error("No data received for category:", categoryId);
            return null;
        }

        const category = {
            id: data._id,
            name: data.name,
            isPromoted: data.isPromoted,
            movies: data.movies || [], // ברירת מחדל לרשימה ריקה אם movies לא קיים
        };

        // console.log("Category fetched successfully:", category);
        return category;
    })
    .catch((error) => {
        console.error("Error fetching category:", error);
        return null;
    });
};

const updateCategory = (categoryId, categoryData) => {
    return fetch(`http://localhost:12345/api/categories/${categoryId}`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "userId": localStorage.getItem('userId'),
        },
        body: JSON.stringify(categoryData),
    })
    .then(response => {
        if (response.status === 204) {
            console.log("Category updated successfully");
            return true;
        } else {
            throw new Error(`Error: ${response.status} - ${response.statusText}`);
        }
    })
    .catch(error => {
        console.error("Error updating category:", error);
        return false;
    });
};

const deleteCategory = (categoryId) => {
    return fetch(`http://localhost:12345/api/categories/${categoryId}`, {
        method: "DELETE",
        headers: {
            "userId": localStorage.getItem('userId'),
        }
    })
    .then(response => {
        if (response.status === 204) {
            alert("Category deleted successfully");
            return true;
        } else {
            throw new Error(`Error: ${response.status} - ${response.statusText}`);
        }
    })
    .catch(error => {
        console.error("Error deleting category:", error);
        return false;
    });
};


export { createCategory, getAllCategories, getCategoryById, deleteCategory, updateCategory};
