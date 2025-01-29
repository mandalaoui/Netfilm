import './TrailerMovieInfo.css';
import { useState, useEffect, useRef } from 'react';

function TrailerMovieInfo({movie}) {
    const [videoSrc, setVideoSrc] = useState('');
    const videoRef = useRef(null);
    
    useEffect(() => {
        if (movie) {
            setVideoSrc(`http://localhost:12345/api/${movie.trailer}`);
        }
    }, [movie]);

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

    return (
            <div className="current-movie-info-container">
                <div className="current-movie-info-trailer">
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
            </div>
    );
}

export default TrailerMovieInfo;
