import { useState, useEffect } from 'react';
import { getAllCategories, getCategoryById } from '../../Category/CategoryActions.js';
import { getMovieById } from '../../Movie/MovieActions.js';

export const AllMovies = () => {
    const [allCategories, setAllCategories] = useState([]);
    const [allMovies, setAllMovies] = useState([]);


    useEffect(() => {
        // Fetch all categories for selection
        const fetchAllCategories = async () => {
            try {
                const categories = await getAllCategories();
                if (categories.length === 0) {
                    return;
                }
                const allCategoryDetails = await Promise.all(
                    categories.map(async (categoryId) => {
                        const fullCategory = await getCategoryById(categoryId);
                        return fullCategory;
                    })
                );
                setAllCategories(allCategoryDetails);
            } catch (error) {
                // console.error("Error fetching all categories:", error);
            }
        };
        fetchAllCategories();
    }, []);

    useEffect(() => {
        const fetchAllMovies = async () => {
            if (!allCategories || allCategories.length === 0) return;
            try {
                const allMoviesId = allCategories.flatMap((category) => category.movies || []);
                if (allMoviesId.length === 0) {
                    return;
                }

                const uniqueMovieIds = [...new Set(allMoviesId)];
                const allMoviesDetails = await Promise.all(
                    uniqueMovieIds.map(async (movieId) => {
                        const fullMovie = await getMovieById(movieId);
                        return fullMovie;
                    })
                );
                const validMovies = allMoviesDetails.filter((movie) => movie !== null);
                setAllMovies(validMovies);
            } catch (error) {
                // console.error("Error fetching all categories:", error);
            }
        };
        if (allCategories.length > 0) {
            fetchAllMovies();
        }
    }, [allCategories]);

    return { allMovies }; 
};
