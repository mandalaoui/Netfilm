import './SmallView.css';
import React, { useRef } from 'react';

function SmallView({ show, onClose, content }) {
    const startSrc = `${getComputedStyle(document.documentElement).getPropertyValue('--FILE_URL')}`;
    const videoRef = useRef(null);
    if (!show) return null;

    return (
        <div className="modal-overlay">
            <div className="small-view-modal-content">
                <button className="modal-close-btn" onClick={onClose}>
                    <i className="bi bi-x-lg"></i>
                </button>
                <div className="modal-body">
                    {content.type === 'video' && content.video && (
                        <div className="modal-video">
                            <video ref={videoRef} src={`${startSrc}${content.video}`} controls />
                        </div>
                    )}
                    {content.type === 'trailer' && content.trailer && (
                        <div className="modal-trailer">
                            <video ref={videoRef} src={`${startSrc}${content.trailer}`} controls />
                        </div>
                    )}
                    {content.type === 'image' && content.image && (
                        <div className="modal-image">
                            <img src={`${startSrc}${content.image}`} alt="Content" />
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default SmallView;