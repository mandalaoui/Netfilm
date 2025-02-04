import './TrailerMovieInfo.css';
import { useState, useEffect, useRef } from 'react';
import { useGlobalContext } from '../../GlobalContext';

function TrailerMovieInfo({movie}) {
    const [videoSrc, setVideoSrc] = useState('');  // State for storing the video source URL
    const videoRef = useRef(null);  // Reference for the video element
    const { fileUrl } = useGlobalContext();

    useEffect(() => {
        // Update the video source when movie data is available
        if (movie) {
            setVideoSrc(`${fileUrl}${movie.trailer}`);
        }
    }, [movie, fileUrl]);

    useEffect(() => {
        // Load and play video when the video source is updated
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
        // console.log("Error loading video:", e);  // Handle error if video fails to load
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
