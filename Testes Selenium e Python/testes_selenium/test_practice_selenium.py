from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service as ChromeService
from webdriver_manager.chrome import ChromeDriverManager

def test_login_sucesso_practice():
    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()))
    driver.implicitly_wait(5)

    driver.get("https://practicetestautomation.com/practice-test-login/")

    driver.find_element(By.ID, "username").send_keys("student") # [cite: 31]
    driver.find_element(By.ID, "password").send_keys("Password123") # [cite: 33]
    driver.find_element(By.ID, "submit").click()

    assert "logged-in-successfully" in driver.current_url
    assert driver.find_element(By.CLASS_NAME, "post-title").text == "Logged In Successfully"

    driver.find_element(By.CSS_SELECTOR, "a[href*='logout']").click()
    assert driver.find_element(By.ID, "submit").is_displayed()
    
    driver.quit()

def test_login_erro_practice():
    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()))
    driver.implicitly_wait(5)
    
    driver.get("https://practicetestautomation.com/practice-test-login/")

    driver.find_element(By.ID, "username").send_keys("wrong_student")
    driver.find_element(By.ID, "password").send_keys("WrongPassword")
    driver.find_element(By.ID, "submit").click()

    error_message = driver.find_element(By.ID, "error")
    assert error_message.is_displayed()
    assert "Your username is invalid!" in error_message.text
    
    driver.quit()