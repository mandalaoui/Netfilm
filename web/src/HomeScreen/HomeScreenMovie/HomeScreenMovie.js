import './HomeScreenMovie.css';
import { useState, useEffect, useRef } from 'react';
import { AllMovies } from '../../Admin/AdminActions/Movie/AllMovies/AllMovies';

function HomeScreenMovie() {
    const { allMovies } = AllMovies();
    const [videoSrc, setVideoSrc] = useState('');
    const [selectedMovie, setSelectedMovie] = useState(null);
    const videoRef = useRef(null);
    console.log("All Movies:", allMovies);

    useEffect(() => {
        if (allMovies.length > 0) {
            const randomIndex = Math.floor(Math.random() * allMovies.length);
            const randomMovie = allMovies[randomIndex];
            setSelectedMovie(randomMovie);
        }
    }, [allMovies]);

    useEffect(() => {
        if (selectedMovie) {
            setVideoSrc(`http://localhost:12345/api/${selectedMovie.trailer}`);
        }
    }, [selectedMovie]);

    useEffect(() => {
        if (videoRef.current && videoSrc) {
            videoRef.current.load();
            videoRef.current.play().catch((error) => {
                console.log("Error playing video:", error);
            });
            console.log("Attempting to play video...");
        }
    }, [videoSrc]);

    const handleVideoLoad = () => {
        console.log("Video has loaded successfully!");
        if (videoRef.current) {
            videoRef.current.muted = false; 
        }
    };

    const handleVideoError = (e) => {
        console.log("Error loading video:", e);
    };

    const navigateTo = (loc) => {
        window.location.href = loc;
    };
    const handleWatchClick = () => {
        navigateTo(`../watchMovie/${selectedMovie._id}`);
    };

    const handleInfoClick = () => {
        navigateTo(`../movie/${selectedMovie._id}`);
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
