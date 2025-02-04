// Function to create a new category
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
            return true;    
        } else {
            alert("Name is not available, try another one");
            throw new Error(`Error: ${response.status} - ${response.statusText}`);
        }
    })
    .catch(error => {
        return false;
    }); 
};

// Function to get all categories
const getAllCategories = () => {
    return fetch("http://localhost:12345/api/categories/", {
        method: "GET",
        headers: {
            "userId": localStorage.getItem('userId'),
        }
    })
    .then(response => response.json())
    .then(data => {
        return data.map((category) => category._id);
    })
    .catch(error => {
        return [];
    });
};

// Function to get a category by its ID
const getCategoryById = (categoryId) => {
    if(!categoryId || categoryId === '') return null;
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
            // alert(`ID is not available, try another one - ${response.status}`);
            // throw new Error(`Error: ${response.status} - ${response.statusText}`);
        }
    })
    .then((data) => {
        if (!data) {
            return null;
        }
        const category = {
            id: data._id,
            name: data.name,
            isPromoted: data.isPromoted,
            movies: data.movies || [],
        };
        return category;
    })
    .catch((error) => {
        return null;
    });
};

// Function to update an existing category
const updateCategory = (categoryId, categoryData) => {
    if(!categoryId || !categoryData) return null;
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
            // alert("Category updated successfully");
            return true;
        } else {
            throw new Error(`Error: ${response.status} - ${response.statusText}`);
        }
    })
    .catch(error => {
        return false;
    });
};

// Function to delete a category by its ID
const deleteCategory = (categoryId) => {
    if(!categoryId || categoryId === '') return null;
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
        return false;
    });
};


export { createCategory, getAllCategories, getCategoryById, deleteCategory, updateCategory};
