
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service as ChromeService
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

def test_login_e_logout_com_sucesso_saucedemo():
    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()))
    driver.implicitly_wait(5)

    driver.get("https://www.saucedemo.com")

    driver.find_element(By.ID, "user-name").send_keys("standard_user")
    driver.find_element(By.ID, "password").send_keys("secret_sauce")
    driver.find_element(By.ID, "login-button").click()

    assert "inventory.html" in driver.current_url
    assert driver.find_element(By.CLASS_NAME, "title").text == "Products"

    driver.find_element(By.ID, "react-burger-menu-btn").click()
    logout_link = WebDriverWait(driver, 10).until(
        EC.element_to_be_clickable((By.ID, "logout_sidebar_link"))
    )
    logout_link.click()

    assert driver.current_url == "https://www.saucedemo.com/"

    driver.quit()

def test_login_invalido_saucedemo():
    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()))

    driver.get("https://www.saucedemo.com")

    driver.find_element(By.ID, "user-name").send_keys("usuario_invalido")
    driver.find_element(By.ID, "password").send_keys("senha_invalida")
    driver.find_element(By.ID, "login-button").click()

    error_message = driver.find_element(By.CSS_SELECTOR, "h3[data-test='error']")
    assert error_message.is_displayed()
    assert "Username and password do not match" in error_message.text

    driver.quit()