import './HomeScreenMovie.css';
import { useState, useEffect, useRef } from 'react';
import { AllMovies } from '../../Admin/AdminActions/Movie/AllMovies/AllMovies';
import { useGlobalContext } from '../../GlobalContext';
import { useNavigate } from 'react-router-dom';

function HomeScreenMovie() {
    const { allMovies } = AllMovies();
    const [videoSrc, setVideoSrc] = useState('');
    const [selectedMovie, setSelectedMovie] = useState(null);
    const videoRef = useRef(null);
    const { fileUrl } = useGlobalContext(); 
    const navigate = useNavigate();


    useEffect(() => {
        if (allMovies.length > 0) {
            const randomIndex = Math.floor(Math.random() * allMovies.length);
            const randomMovie = allMovies[randomIndex];
            setSelectedMovie(randomMovie);
        }
    }, [allMovies]);

    useEffect(() => {
        if (selectedMovie) {
            setVideoSrc(`${fileUrl}${selectedMovie.trailer}`);
        }
    }, [selectedMovie, fileUrl]);

    useEffect(() => {
        if (videoRef.current && videoSrc) {
            videoRef.current.load();
            videoRef.current.play().catch((error) => {
            });
        }
    }, [videoSrc]);

    const handleVideoLoad = () => {
        if (videoRef.current) {
            videoRef.current.muted = false; 
        }
    };

    const handleVideoError = (e) => {
        // alert("Error loading video:", e);
    };

    const handleWatchClick = () => {
        navigate(`../watchMovie/${selectedMovie._id}`);
    };

    const handleInfoClick = () => {
        navigate(`../movie/${selectedMovie._id}`);
    };

    return (
        <div className="video-random-container">
            <div className="video-random">
                <video
                    ref={videoRef}
                    autoPlay
                    muted
                    onCanPlay={handleVideoLoad}
                    onError={handleVideoError}
                >
                    <source src={videoSrc} type="video/mp4" />
                </video>
            </div>
            {selectedMovie && (
                <>
                    <div className="video-random-details">
                        <div className="random-movie-title-overlay">
                            {selectedMovie.name}
                        </div>
                        <div className="random-movie-buttons">
                            <button className="watch-btn" onClick={handleWatchClick}>Watch</button>
                            <button className="info-btn" onClick={handleInfoClick}>More Info</button>
                        </div>

                    </div>
                </>
            )}
        </div>
    );
}

export default HomeScreenMovie;
