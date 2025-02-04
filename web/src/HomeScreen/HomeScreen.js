import './HomeScreen.css';
import UpperMenu from './UpperMenu/UpperMenu.js';
import HomeScreenMovie from './HomeScreenMovie/HomeScreenMovie.js';
import Movies from './Movies/Movies.js';
import Search from './Search/Search.js';
import MoviesBy from './MoviesBy/MoviesBy.js';
import { useLocation } from 'react-router-dom';
import { LocationProvider } from '../LocationContext.js';
import { useState, useEffect } from 'react';
import { getAllCategories, getCategoryById } from '../Admin/AdminActions/Category/CategoryActions.js';

function HomeScreen() {
    const location = useLocation(); // Get current location of the app
    const [searchQuery, setSearchQuery] = useState(''); // State to hold search query
    const [showMoviesBy, setShowMoviesBy] = useState(false); // State to hold movies filter
    const [selectedCategory, setSelectedCategory] = useState(null); // State to hold selected category
    const [categories, setCategories] = useState([]);

    // Fetch categories from the server when the component mounts
    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const categoryList = await getAllCategories();
                if (categoryList && Array.isArray(categoryList)) {
                    const categoryObjects = [];
                    // Loop over category IDs to get each category details
                    for (const id of categoryList) {
                        const category = await getCategoryById(id);
                        if (category && category.name !== "unAttached") {
                            categoryObjects.push(category);
                        }
                    }
                    setCategories(categoryObjects);
                } else {
                    console.error("categoryList is not an array:", categoryList);
                }
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };
        fetchCategories();
    }, []);

    // Function to handle changes in search input
    const handleSearchChange = (event) => {
        setSearchQuery(event.target.value);
        setShowMoviesBy(false);
        setSelectedCategory(null);
    };

    // Fuction to handle MoviesBy button click
    const handleMoviesByClick = () => {
        setShowMoviesBy(!showMoviesBy);
        setSearchQuery('');
        setSelectedCategory(null);
    };

    // Function to handle category selection
    const handleCategorySelect = async (categoryId) => {
        try {
            const category = await getCategoryById(categoryId); // חכה שהתוצאה תחזור
            setSelectedCategory(category);
        } catch (error) {
            console.error("Error fetching category:", error);
        }
    };

    return (
        <LocationProvider location={location}>
            <div className="home-container">
                <UpperMenu
                    searchQuery={searchQuery}
                    onSearchChange={handleSearchChange}
                    setShowMoviesBy={handleMoviesByClick}
                />
                {searchQuery !== '' ? (
                    <Search query={searchQuery} />
                ) : showMoviesBy ? (
                    <div className='movies-by-container'>
                        <select
                            className="category-select"
                            onChange={(e) => handleCategorySelect(e.target.value)}
                        >
                            <option className="category-option" value="">Select a Category</option>
                            {categories.map((category) => (
                                <option
                                    className="category-option"
                                    key={category.id}
                                    value={category.id}
                                >
                                    {category.name}
                                </option>
                            ))}
                        </select>
                        {selectedCategory && <MoviesBy category={selectedCategory} />}
                    </div>
                ) : (
                    <>
                        <HomeScreenMovie />
                        <Movies />
                    </>
                )}
                <div className="bottom-space"></div>
            </div>
        </LocationProvider>
    );
}

export default HomeScreen;
