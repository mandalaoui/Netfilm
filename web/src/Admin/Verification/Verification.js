import ReactDOM from 'react-dom/client'; 
import './Verification.css';

function Verification({ type, title, actionType, onConfirm }) {
    const handleConfirmation = (confirmed) => {
        onConfirm(confirmed); 
        const modal = document.querySelector('.confirmation-modal');
        if (modal) {
            modal.remove();  
        }
        document.body.style.overflow = 'auto'; 
    };

    return (
        <div className="confirmation-modal">
            <div className="confirmation-content">
                <p>Are you sure you want to {actionType} the {type} "{title}"?</p>
                <button className="confirm-yes" onClick={() => handleConfirmation(true)}>Yes</button>
                <button className="confirm-no" onClick={() => handleConfirmation(false)}>No</button>
            </div>
        </div>
    );
}

export const showConfirmationModal = (type, title, actionType) => {
    return new Promise((resolve) => {
        const modalContainer = document.createElement('div');
        modalContainer.className = 'modal-container';
        document.body.appendChild(modalContainer);

        const handleConfirm = (confirmed) => {
            resolve(confirmed);  
            document.body.removeChild(modalContainer);  
        };

        const rootElement = document.createElement('div');
        modalContainer.appendChild(rootElement); 

        const root = ReactDOM.createRoot(rootElement);
        root.render(<Verification type={type} title={title} actionType={actionType} onConfirm={handleConfirm} />);

        document.body.style.overflow = 'hidden'; 
    });
};
